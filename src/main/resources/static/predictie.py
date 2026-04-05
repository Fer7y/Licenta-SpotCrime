import mysql.connector
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.ensemble import RandomForestRegressor
from sklearn.svm import SVR
from datetime import datetime
import warnings

# Ignoram avertismentele de tip deprecation pentru un terminal mai curat
warnings.filterwarnings('ignore')

def genereaza_predictii():
    # 1. Conectarea la baza de date
    try:
        db = mysql.connector.connect(
            host="localhost",
            user="root",
            password="1234",
            database="crimerate_db" # AICI MODIFICI CU NUMELE BAZEI TALE DE DATE
        )
        cursor = db.cursor(dictionary=True)
        print("Conectare la baza de date reusita!")
    except Exception as e:
        print(f"Eroare la conectare: {e}")
        return

    # 2. Preluarea complet automata a anului prezent
    anul_curent = datetime.now().year

    # 3. Preluam lista de judete/sectoare
    cursor.execute("SELECT id, nume_judet FROM judet")
    judete = cursor.fetchall()

    for judet in judete:
        id_judet = judet['id']
        nume_judet = judet['nume_judet']

        # Preluam datele DOAR din tabelul oficial istoric_criminalitate
        cursor.execute(f"SELECT an, coeficient FROM istoric_criminalitate WHERE id_judet = {id_judet} ORDER BY an ASC")
        istoric = cursor.fetchall()

        if len(istoric) < 4:
            print(f"Sarit: {nume_judet} nu are destule date oficiale pentru a antrena modelele.")
            continue

        df = pd.DataFrame(istoric)
        X = df[['an']].values
        y = df['coeficient'].values

        # 4. Impartirea datelor in set de antrenare (80%) si validare (20%)
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

        # 5. Instantierea celor 3 algoritmi diferiti
        model_lr = LinearRegression()
        model_rf = RandomForestRegressor(n_estimators=100, random_state=42)
        model_svr = SVR(kernel='rbf', C=100, gamma=0.1)

        # Antrenam modelele
        model_lr.fit(X_train, y_train)
        model_rf.fit(X_train, y_train)
        model_svr.fit(X_train, y_train)

        # Reantrenam pe toate datele disponibile pentru acuratete maxima pe viitor
        model_lr.fit(X, y)
        model_rf.fit(X, y)
        model_svr.fit(X, y)

        # 6. Facem predictia pentru anul gasit in sistem (2026)
        an_tinta = [[anul_curent]]
        pred_lr = model_lr.predict(an_tinta)[0]
        pred_rf = model_rf.predict(an_tinta)[0]
        pred_svr = model_svr.predict(an_tinta)[0]

        # 7. Realizam media (tehnica Ensemble) intre cei 3 algoritmi
        predictie_finala = (pred_lr + pred_rf + pred_svr) / 3
        predictie_finala = round(predictie_finala, 4)

        # 8. Salvam in baza de date
        cursor.execute(f"SELECT id FROM predictie_ml WHERE id_judet = {id_judet} AND an_vizat = {anul_curent}")
        exista = cursor.fetchone()

        if exista:
            cursor.execute(f"UPDATE predictie_ml SET coeficient_prezis = {predictie_finala}, data_generare = CURRENT_TIMESTAMP WHERE id_judet = {id_judet} AND an_vizat = {anul_curent}")
        else:
            cursor.execute(f"INSERT INTO predictie_ml (id_judet, an_vizat, coeficient_prezis) VALUES ({id_judet}, {anul_curent}, {predictie_finala})")

        db.commit()
        print(f"Model antrenat pentru {nume_judet}. Predictie {anul_curent}: {predictie_finala}")

    cursor.close()
    db.close()
    print("Inteligenta Artificiala a terminat procesarea. Toate predictiile sunt in baza de date!")

if __name__ == "__main__":
    genereaza_predictii()