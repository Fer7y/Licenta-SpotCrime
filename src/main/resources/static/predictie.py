import mysql.connector
import pandas as pd
from sklearn.linear_model import LinearRegression
from sklearn.ensemble import RandomForestRegressor
from sklearn.svm import SVR
from datetime import datetime
import warnings
import json
import sys

warnings.filterwarnings('ignore')

def genereaza_predictii():
    try:
        # Datele tale de conectare la baza de date
        db = mysql.connector.connect(
            host="localhost",
            user="root",
            password="1234",
            database="crimerate_db"
        )
        cursor = db.cursor(dictionary=True)
    except Exception as e:
        print(json.dumps([{"eroare": str(e)}]))
        sys.exit(1)

    anul_curent = datetime.now().year
    cursor.execute("SELECT id, nume_judet FROM judet")
    judete = cursor.fetchall()

    rezultate_finale = []

    for judet in judete:
        id_judet = judet['id']
        nume_judet = judet['nume_judet']

        cursor.execute(f"SELECT an, coeficient FROM istoric_criminalitate WHERE id_judet = {id_judet} ORDER BY an ASC")
        istoric = cursor.fetchall()

        if len(istoric) < 4:
            continue # Sărim peste dacă nu are istoric suficient

        df = pd.DataFrame(istoric)
        X = df[['an']].values
        y = df['coeficient'].values

        model_lr = LinearRegression()
        model_rf = RandomForestRegressor(n_estimators=100, random_state=42)
        model_svr = SVR(kernel='rbf', C=100, gamma=0.1)

        model_lr.fit(X, y)
        model_rf.fit(X, y)
        model_svr.fit(X, y)

        an_tinta = [[anul_curent]]
        pred_lr = model_lr.predict(an_tinta)[0]
        pred_rf = model_rf.predict(an_tinta)[0]
        pred_svr = model_svr.predict(an_tinta)[0]

        # Facem media celor 3 algoritmi (Ensemble)
        predictie_finala = round((pred_lr + pred_rf + pred_svr) / 3, 4)

        rezultate_finale.append({
            "nume_judet": nume_judet,
            "an_vizat": anul_curent,
            "coeficient_prezis": predictie_finala,
            "data_generare": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        })

    cursor.close()
    db.close()

    # Sortăm descrescător ca să avem topul județelor periculoase
    rezultate_finale = sorted(rezultate_finale, key=lambda x: x['coeficient_prezis'], reverse=True)

    # TRUCUL: Returnăm datele direct către Java sub formă de text
    print(json.dumps(rezultate_finale))

if __name__ == "__main__":
    genereaza_predictii()