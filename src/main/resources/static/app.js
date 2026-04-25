// ==========================================
// --- CONSTANTE API ---
// ==========================================
const API_URL = "http://localhost:8080/api/auth";
const API_INCIDENTE = "http://localhost:8080/api/incidente";
const API_ISTORIC = "http://localhost:8080/api/istoric";
const API_ABONAMENTE = "http://localhost:8080/api/abonamente";
const API_ADMIN = "http://localhost:8080/api/admin";
const API_NOTIF = "http://localhost:8080/api/notificari";
const API_APLICATII = "http://localhost:8080/api/aplicatii";
const API_UTILIZATORI = "http://localhost:8080/api/utilizatori";

// ==========================================
// --- FUNCTIE PENTRU POP-UP FRUMOS (TOAST) ---
// ==========================================
function showToast(message, type = 'error') {
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerText = message;
    document.body.appendChild(toast);

    setTimeout(() => toast.classList.add('show'), 10);
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 400);
    }, 3000);
}

// ==========================================
// --- 1. INITIALIZARE LA INCARCAREA PAGINII ---
// ==========================================
document.addEventListener("DOMContentLoaded", () => {
    const userData = localStorage.getItem("user");
    const menuNotificari = document.getElementById("menuNotificari");

    const loginItem = document.getElementById("loginMenuItem");
    const logoutItem = document.getElementById("logoutMenuItem");
    const raportareItem = document.getElementById("raportareMenuItem");
    const adminItem = document.getElementById("adminMenuItem");
    const predictiiItem = document.getElementById("predictiiMenuItem");
    const exportItem = document.getElementById("exportMenuItem");

    const profileHeaderBtn = document.getElementById("profileHeaderBtn");
    const profileHeaderText = document.getElementById("profileHeaderText");

    const aplicaAdminItem = document.getElementById("aplicaAdminMenuItem");
    const adminSupremItem = document.getElementById("adminSupremMenuItem");

    if (userData) {
        const user = JSON.parse(userData);
        if(loginItem) loginItem.style.display = "none";
        if(logoutItem) logoutItem.style.display = "block";
        if(raportareItem) raportareItem.style.display = "block";
        if(menuNotificari) menuNotificari.style.display = "block";


        if(aplicaAdminItem) {
            aplicaAdminItem.style.display = (user.rol === "CETATEAN") ? "block" : "none";
        }
        if(adminItem) {
            adminItem.style.display = (user.rol === "ADMIN" || user.rol === "ADMIN_SUPREM") ? "block" : "none";
        }
        if(adminSupremItem) {
            adminSupremItem.style.display = (user.rol === "ADMIN_SUPREM") ? "block" : "none";
        }

        if(predictiiItem) predictiiItem.style.display = "block";
        if(exportItem) exportItem.style.display = "block";

        if(profileHeaderText) profileHeaderText.innerText = "Contul Meu";
        if(profileHeaderBtn) {
            profileHeaderBtn.href = "profil.html";
            profileHeaderBtn.style.cursor = "pointer";
            profileHeaderBtn.onclick = null;
        }
    } else {
        if(loginItem) loginItem.style.display = "block";
        if(logoutItem) logoutItem.style.display = "none";
        if(raportareItem) raportareItem.style.display = "none";
        if(adminItem) adminItem.style.display = "none";
        if(menuNotificari) menuNotificari.style.display = "none";
        if(aplicaAdminItem) aplicaAdminItem.style.display = "none";
        if(adminSupremItem) adminSupremItem.style.display = "none";
        if(predictiiItem) predictiiItem.style.display = "none";
        if(exportItem) exportItem.style.display = "none";

        if(profileHeaderText) profileHeaderText.innerText = "Guest";
        if(profileHeaderBtn) {
            profileHeaderBtn.href = "#";
            profileHeaderBtn.style.cursor = "default";
            profileHeaderBtn.onclick = (e) => e.preventDefault();
        }
    }

    // Apelează funcțiile specifice paginilor doar dacă găsește elementele lor HTML
    if (document.getElementById("profNume")) incarcaDateProfil();
    if (document.getElementById("globalYearSelect")) {
        incarcaDashboard();
        if (document.getElementById("tabelYearSelect")) incarcaTabelSelectat();
        populeazaDropdownJudete();
    }
    if (document.getElementById("map")) {
        initializeazaHarta();
        incarcaDateHarta();
    }
    if (document.getElementById("miniMap")) initMiniMap();
    if (document.getElementById("tabelAdminBody")) incarcaIncidenteAdmin();
    if (document.getElementById("boxFormularAplicatie")) incarcaPaginaAplicaAdmin();
    if (document.getElementById("gridAplicatii"))
    {
            incarcaAplicatiiSuprem();
            incarcaListaUtilizatori();
        }

    // NOU: Dacă suntem pe pagina de predicții, pornim inteligența artificială
    if (document.getElementById("loadingPredictii")) incarcaDatePredictiiDinamice();
});

// ==========================================
// --- 2. AUTENTIFICARE ---
// ==========================================
function logout() {
    localStorage.removeItem("user");
    window.location.href = "index.html";
}

function toggleForms() {
    const loginForm = document.getElementById("loginFormContainer");
    const regForm = document.getElementById("registerFormContainer");
    if (loginForm.style.display === "none") {
        loginForm.style.display = "block";
        regForm.style.display = "none";
    } else {
        loginForm.style.display = "none";
        regForm.style.display = "block";
    }
}

async function faceLogin() {
    const email = document.getElementById("loginEmail").value;
    const parola = document.getElementById("loginParola").value;

    try {
        const response = await fetch(`${API_URL}/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, parola })
        });
        if (response.ok) {
            localStorage.setItem("user", JSON.stringify(await response.json()));
            window.location.href = "index.html";
        } else {
            showToast(await response.text(), 'error');
        }
    } catch (err) { showToast("Eroare de conexiune la server!", 'error'); }
}

async function faceRegister() {
    const numeComplet = document.getElementById("regNume").value;
    const email = document.getElementById("regEmail").value;
    const parola = document.getElementById("regParola").value;

    try {
        const response = await fetch(`${API_URL}/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ numeComplet, email, parola })
        });
        if (response.ok) {
            localStorage.setItem("user", JSON.stringify(await response.json()));
            showToast("Cont creat cu succes! Redirecționare...", 'success');
            setTimeout(() => window.location.href = "index.html", 1500);
        } else {
            showToast(await response.text(), 'error');
        }
    } catch (err) { showToast("Eroare de conexiune!", 'error'); }
}

// ==========================================
// --- 3. PROFIL & ALERTE ---
// ==========================================
async function incarcaDateProfil() {
    const userData = localStorage.getItem("user");
    if (!userData) { window.location.href = "login.html"; return; }
    const user = JSON.parse(userData);

    document.getElementById("profNume").innerText = user.numeComplet;
    document.getElementById("profRol").innerText = user.rol;
    document.getElementById("profEmail").innerText = user.email;

    if(user.dataInregistrare) {
        document.getElementById("profData").innerText = new Date(user.dataInregistrare).toLocaleDateString("ro-RO");
    } else {
        document.getElementById("profData").innerText = "Recent";
    }

    try {
        const response = await fetch(`${API_INCIDENTE}/utilizator/${user.id}`);
        if (response.ok) {
            const incidente = await response.json();
            document.getElementById("statTotal").innerText = incidente.length;
            document.getElementById("statConfirmate").innerText = incidente.filter(i => i.status === 'APROBAT').length;
            document.getElementById("statFalse").innerText = incidente.filter(i => i.status === 'RESPINS').length;
        }
    } catch (err) { console.error(err); }

    incarcaAbonamente(user.id);
}

async function incarcaAbonamente(userId) {
    try {
        const response = await fetch(`${API_ABONAMENTE}/utilizator/${userId}`);
        if (response.ok) {
            const abonamente = await response.json();
            const container = document.getElementById("listaAbonamente");
            container.innerHTML = "";

            if (abonamente.length === 0) {
                container.innerHTML = '<span style="color: var(--text-muted); font-size: 0.95rem;">Nu ești abonat la niciun oraș. Caută mai sus!</span>';
                return;
            }

            abonamente.forEach(ab => {
                container.innerHTML += `
                    <div class="abonament-badge">
                        <span>${ab.numeLocalitate} (${ab.numeJudet})</span>
                        <i class="fa-solid fa-circle-xmark" onclick="stergeAbonament(${ab.id})" title="Șterge alertă"></i>
                    </div>
                `;
            });
        }
    } catch (err) { console.error("Eroare aducere abonamente", err); }
}

let searchTimeout;
async function cautaOras(text) {
    const resultsBox = document.getElementById("searchResults");
    if (!text || text.length < 2) {
        resultsBox.style.display = "none";
        return;
    }

    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(async () => {
        try {
            const response = await fetch(`${API_ABONAMENTE}/cauta?nume=${text}`);
            if (response.ok) {
                const orase = await response.json();
                resultsBox.innerHTML = "";

                if (orase.length === 0) {
                    resultsBox.innerHTML = '<li style="color: var(--text-muted); cursor: default;">Niciun oraș găsit...</li>';
                } else {
                    orase.forEach(oras => {
                        resultsBox.innerHTML += `<li onclick="adaugaAbonament(${oras.id})"><i class="fa-solid fa-location-dot" style="margin-right: 8px; color: var(--text-muted);"></i> ${oras.numeLocalitate} (${oras.numeJudet})</li>`;
                    });
                }
                resultsBox.style.display = "block";
            }
        } catch (e) { console.error(e); }
    }, 300);
}

async function adaugaAbonament(localitateId) {
    const userData = JSON.parse(localStorage.getItem("user"));
    if (!userData) return;

    document.getElementById("searchResults").style.display = "none";
    document.getElementById("searchOras").value = "";

    try {
        const response = await fetch(`${API_ABONAMENTE}/adauga?userId=${userData.id}&localitateId=${localitateId}`, {
            method: 'POST'
        });

        if (response.ok) {
            showToast(await response.text(), "success");
            incarcaAbonamente(userData.id);
        } else {
            showToast(await response.text(), "error");
        }
    } catch (e) { showToast("Eroare de conexiune!", "error"); }
}

async function stergeAbonament(abonamentId) {
    const userData = JSON.parse(localStorage.getItem("user"));
    if (!userData) return;

    try {
        const response = await fetch(`${API_ABONAMENTE}/sterge/${abonamentId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showToast(await response.text(), "success");
            incarcaAbonamente(userData.id);
        } else {
            showToast("Eroare la ștergere", "error");
        }
    } catch (e) { showToast("Eroare de conexiune!", "error"); }
}

document.addEventListener('click', function(event) {
    const searchInput = document.getElementById('searchOras');
    const resultsBox = document.getElementById('searchResults');

    if (searchInput && !searchInput.contains(event.target) && !resultsBox.contains(event.target)) {
        resultsBox.style.display = 'none';
    }
});

// ==========================================
// --- 4. SCHIMBARE PAROLĂ ---
// ==========================================
function arataModalParola() { document.getElementById('parolaModal').classList.add('show'); }
function ascundeModalParola() {
    document.getElementById('parolaModal').classList.remove('show');
    document.getElementById('oldPassword').value = '';
    document.getElementById('newPassword').value = '';
    document.getElementById('confirmPassword').value = '';
}

async function salveazaParola() {
    const oldPass = document.getElementById('oldPassword').value;
    const newPass = document.getElementById('newPassword').value;
    const confPass = document.getElementById('confirmPassword').value;

    if (!oldPass || !newPass || !confPass) return showToast("Te rog completează toate câmpurile!", "error");
    if (newPass !== confPass) return showToast("Parolele noi nu coincid!", "error");
    if (newPass.length < 6) return showToast("Parola nouă trebuie să aibă minim 6 caractere!", "error");

    const userData = JSON.parse(localStorage.getItem("user"));
    if (!userData) return;

    try {
        const response = await fetch(`${API_URL}/${userData.id}/schimba-parola`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ parolaVeche: oldPass, parolaNoua: newPass })
        });
        if (response.ok) {
            showToast(await response.text(), "success");
            ascundeModalParola();
        } else {
            showToast(await response.text(), "error");
        }
    } catch (err) { showToast("Eroare de conexiune!", "error"); }
}

// ==========================================
// --- 5. DASHBOARD & GRAFICE ---
// ==========================================
let top5ChartInstance = null;
let bottom5ChartInstance = null;
let evolutieChartInstance = null;

async function incarcaDashboard() {
    const anCurent = parseInt(document.getElementById("globalYearSelect").value);

    try {
        const resCurent = await fetch(`${API_ISTORIC}/an/${anCurent}`);

        if (resCurent.ok) {
            const dateCurente = await resCurent.json();
            if (!dateCurente || dateCurente.length === 0) return;
            deseneazaTopBottom(dateCurente);
        }
    } catch (err) { console.error(err); }
}

function deseneazaTopBottom(date) {
    try {
        const dateSortate = [...date].sort((a, b) => Number(b.coeficient) - Number(a.coeficient));
        const top5 = dateSortate.slice(0, 5);
        const bottom5 = [...dateSortate].reverse().slice(0, 5);

        const ctxTop = document.getElementById('top5Chart').getContext('2d');
        if (top5ChartInstance) top5ChartInstance.destroy();
        top5ChartInstance = new Chart(ctxTop, {
            type: 'bar',
            data: {
                labels: top5.map(d => d.numeJudet),
                datasets: [{ label: 'Coeficient Risc', data: top5.map(d => Number(d.coeficient)), backgroundColor: 'rgba(239, 68, 68, 0.8)', borderRadius: 6 }]
            },
            options: { plugins: { legend: { display: false } } }
        });

        const ctxBottom = document.getElementById('bottom5Chart').getContext('2d');
        if (bottom5ChartInstance) bottom5ChartInstance.destroy();
        bottom5ChartInstance = new Chart(ctxBottom, {
            type: 'bar',
            data: {
                labels: bottom5.map(d => d.numeJudet),
                datasets: [{ label: 'Coeficient Risc', data: bottom5.map(d => Number(d.coeficient)), backgroundColor: 'rgba(16, 185, 129, 0.8)', borderRadius: 6 }]
            },
            options: { plugins: { legend: { display: false } } }
        });
    } catch (err) { console.error(err); }
}

function populeazaTabel(dateCurente, datePrecedente) {
    const tbody = document.getElementById("tabelJudeteBody");
    tbody.innerHTML = "";

    const dateSortate = [...dateCurente].sort((a, b) => (a.numeJudet || "").localeCompare(b.numeJudet || ""));

    dateSortate.forEach(judet => {
        const judetAnterior = datePrecedente.find(j => j.idJudet === judet.idJudet);
        let trendHtml = `<span class="trend-neutral">- Date insuficiente</span>`;

        if (judetAnterior) {
            const dif = (Number(judet.coeficient) - Number(judetAnterior.coeficient)).toFixed(2);
            if (dif > 0) trendHtml = `<span class="trend-up"><i class="fa-solid fa-arrow-trend-up"></i> +${dif}% (Creștere)</span>`;
            else if (dif < 0) trendHtml = `<span class="trend-down"><i class="fa-solid fa-arrow-trend-down"></i> ${dif}% (Scădere)</span>`;
            else trendHtml = `<span class="trend-neutral">Stagnează</span>`;
        }

        let badgeClass = "badge-mediu";
        if (judet.domeniuIncadrare === 'SCAZUT') badgeClass = "badge-scazut";
        if (judet.domeniuIncadrare === 'RIDICAT') badgeClass = "badge-ridicat";

        tbody.innerHTML += `
            <tr>
                <td style="font-weight: 500;">${judet.numeJudet}</td>
                <td><strong>${judet.coeficient}</strong></td>
                <td><span class="badge ${badgeClass}">${judet.domeniuIncadrare}</span></td>
                <td>${trendHtml}</td>
            </tr>
        `;
    });
}

async function populeazaDropdownJudete() {
    try {
        const res = await fetch(`${API_ISTORIC}/an/2025`);
        if (res.ok) {
            const judete = await res.json();
            const select = document.getElementById("judetEvolutieSelect");
            select.innerHTML = "";
            judete.sort((a, b) => (a.numeJudet || "").localeCompare(b.numeJudet || "")).forEach(j => {
                select.innerHTML += `<option value="${j.idJudet}">${j.numeJudet}</option>`;
            });

            select.innerHTML += `<option value="48">Municipiul București</option>`;

            if (judete.length > 0) incarcaGraficEvolutie();
        }
    } catch (e) { console.error(e); }
}

async function incarcaGraficEvolutie() {
    const idJudet = document.getElementById("judetEvolutieSelect").value;
    const numeJudet = document.getElementById("judetEvolutieSelect").options[document.getElementById("judetEvolutieSelect").selectedIndex].text;

    try {
        const res = await fetch(`${API_ISTORIC}/judet/${idJudet}`);
        if (res.ok) {
            const istoric = await res.json();
            const ctx = document.getElementById('evolutieChart').getContext('2d');
            if (evolutieChartInstance) evolutieChartInstance.destroy();

            evolutieChartInstance = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: istoric.map(d => d.an),
                    datasets: [{
                        label: `Coeficient ${numeJudet}`,
                        data: istoric.map(d => Number(d.coeficient)),
                        borderColor: '#3b82f6',
                        backgroundColor: 'rgba(59, 130, 246, 0.1)',
                        borderWidth: 3,
                        fill: true,
                        tension: 0.3
                    }]
                }
            });
        }
    } catch (e) { console.error(e); }
}

async function incarcaTabelSelectat() {
    const selectTabel = document.getElementById("tabelYearSelect");
    if (!selectTabel) return;

    const anCurent = parseInt(selectTabel.value);
    const anPrecedent = anCurent - 1;

    try {
        const resCurent = await fetch(`${API_ISTORIC}/an/${anCurent}`);
        const resPrecedent = await fetch(`${API_ISTORIC}/an/${anPrecedent}`);

        if (resCurent.ok) {
            const dateCurente = await resCurent.json();
            const datePrecedente = resPrecedent.ok ? await resPrecedent.json() : [];
            populeazaTabel(dateCurente, datePrecedente);
        }
    } catch (err) { console.error(err); }
}

// ==========================================
// --- 6. HARTĂ (Leaflet) ---
// ==========================================
let mapInstance = null;
let markersLayer = null;

function initializeazaHarta() {
    try {
        mapInstance = L.map('map').setView([45.9000, 24.9668], 7);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors',
            maxZoom: 18,
        }).addTo(mapInstance);
        markersLayer = L.layerGroup().addTo(mapInstance);
    } catch (e) {
        console.error("[DEBUG HARTĂ] Eroare la inițializarea hărții Leaflet:", e);
    }
}

async function incarcaDateHarta() {
    const selectElement = document.getElementById("hartaYearSelect");
    if (!selectElement) return;

    const anSelectat = parseInt(selectElement.value);

    try {
        const resIncidente = await fetch(`${API_INCIDENTE}/aprobate`);
        if (resIncidente.ok) {
            const incidenteTotiAnii = await resIncidente.json();
            const incidenteFiltrate = incidenteTotiAnii.filter(inc => {
                if (!inc.dataRaportare) return true;
                return new Date(inc.dataRaportare).getFullYear() === anSelectat;
            });
            punePionezePeHarta(incidenteFiltrate);
        }
    } catch (e) { console.error("Eroare fetch incidente:", e); }
}

function punePionezePeHarta(incidente) {
    markersLayer.clearLayers();
    incidente.forEach(inc => {
        if (inc.latitudine && inc.longitudine) {
            const dataInc = new Date(inc.dataRaportare).toLocaleDateString("ro-RO", {
                year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit'
            });

            const popupContinut = `
                <div class="popup-custom">
                    <h3 style="margin:0; color:#3b82f6;">${inc.tipInfractiune || 'Incident'}</h3>
                    <p style="margin:8px 0;">${inc.descriere || 'Fără descriere'}</p>
                    <span style="font-size:0.8rem; color:#94a3b8;"><i class="fa-solid fa-clock"></i> ${dataInc}</span>
                </div>
            `;

            const marker = L.marker([inc.latitudine, inc.longitudine]).bindPopup(popupContinut);
            markersLayer.addLayer(marker);
        }
    });
}
// ==========================================
// --- 7. ZONA RAPORTARE INCIDENT ---
// ==========================================
let miniMapInstance = null;
let rapMarker = null;

function initMiniMap() {
    miniMapInstance = L.map('miniMap').setView([45.9000, 24.9668], 6);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution: '© OpenStreetMap' }).addTo(miniMapInstance);

    miniMapInstance.on('click', function(e) {
        const lat = e.latlng.lat;
        const lng = e.latlng.lng;

        document.getElementById('rapLat').value = lat;
        document.getElementById('rapLng').value = lng;

        if (rapMarker) { miniMapInstance.removeLayer(rapMarker); }

        const redIcon = new L.Icon({
            iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
            shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
            iconSize: [25, 41], iconAnchor: [12, 41], popupAnchor: [1, -34], shadowSize: [41, 41]
        });

        rapMarker = L.marker([lat, lng], {icon: redIcon}).addTo(miniMapInstance);
    });
}

let searchRapTimeout;
async function cautaOrasRaportare(text) {
    const resultsBox = document.getElementById("rapSearchResults");
    if (!text || text.length < 2) {
        resultsBox.style.display = "none";
        return;
    }

    clearTimeout(searchRapTimeout);
    searchRapTimeout = setTimeout(async () => {
        try {
            const response = await fetch(`${API_ABONAMENTE}/cauta?nume=${text}`);
            if (response.ok) {
                const orase = await response.json();
                resultsBox.innerHTML = "";
                if (orase.length === 0) {
                    resultsBox.innerHTML = '<li style="color: var(--text-muted);">Niciun oraș găsit...</li>';
                } else {
                    orase.forEach(oras => {
                        resultsBox.innerHTML += `<li onclick="selecteazaOrasRaportare(${oras.id}, '${oras.numeLocalitate}', '${oras.numeJudet}')"><i class="fa-solid fa-location-dot" style="margin-right:8px;"></i> ${oras.numeLocalitate} (${oras.numeJudet})</li>`;
                    });
                }
                resultsBox.style.display = "block";
            }
        } catch (e) { console.error(e); }
    }, 300);
}

function selecteazaOrasRaportare(id, nume, judet) {
    document.getElementById("rapLocalitateId").value = id;
    document.getElementById("rapSearchOras").value = "";
    document.getElementById("rapSearchResults").style.display = "none";

    const textSelectat = document.getElementById("rapOrasSelectatText");
    textSelectat.querySelector("span").innerText = `${nume} (Județul ${judet})`;
    textSelectat.style.display = "block";
}

async function trimiteRaport() {
    const userData = JSON.parse(localStorage.getItem("user"));
    if (!userData) {
        showToast("Trebuie să fii autentificat pentru a raporta un incident!", "error");
        setTimeout(() => window.location.href = "login.html", 2000);
        return;
    }

    const idLocalitate = document.getElementById("rapLocalitateId").value;
    let tipInfractiune = document.getElementById("rapTip").value;
    const descriere = document.getElementById("rapDescriere").value;
    const lat = document.getElementById("rapLat").value;
    const lng = document.getElementById("rapLng").value;

    if (tipInfractiune === "Altele") {
        tipInfractiune = document.getElementById("rapAltTip").value.trim();
        if (!tipInfractiune) {
            return showToast("Te rugăm să specifici tipul infracțiunii!", "error");
        }
    }

    if (!tipInfractiune) return showToast("Alege tipul infracțiunii!", "error");
    if (!idLocalitate) return showToast("Caută și selectează un oraș!", "error");
    if (!lat || !lng) return showToast("Dă click pe hartă pentru a marca locația!", "error");

    const cerere = {
        idUtilizator: userData.id,
        idLocalitate: parseInt(idLocalitate),
        tipInfractiune: tipInfractiune,
        descriere: descriere,
        latitudine: parseFloat(lat),
        longitudine: parseFloat(lng)
    };

    try {
        const response = await fetch(`${API_INCIDENTE}/raporteaza`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(cerere)
        });

        if (response.ok) {
            showToast(await response.text(), "success");
            document.getElementById("rapTip").value = "";
            const inputAltTip = document.getElementById("rapAltTip");
            if(inputAltTip) inputAltTip.value = "";
            const containerAltTip = document.getElementById("containerAltTip");
            if(containerAltTip) containerAltTip.style.display = "none";
            document.getElementById("rapDescriere").value = "";
            document.getElementById("rapSearchOras").value = "";
            document.getElementById("rapLocalitateId").value = "";
            document.getElementById("rapOrasSelectatText").style.display = "none";

            if (typeof rapMarker !== 'undefined' && rapMarker) {
                miniMapInstance.removeLayer(rapMarker);
                rapMarker = null;
            }
            document.getElementById("rapLat").value = "";
            document.getElementById("rapLng").value = "";
        } else {
            showToast(await response.text(), "error");
        }
    } catch (e) {
        showToast("Eroare de conexiune la server!", "error");
    }
}

// ==========================================
// --- 8. ZONA PENTRU PANOUL DE ADMIN ---
// ==========================================
async function incarcaIncidenteAdmin() {
    const tbody = document.getElementById("tabelAdminBody");
    if (!tbody) return;

    try {
        const response = await fetch(`${API_ADMIN}/incidente-in-asteptare`);
        const incidente = await response.json();
        tbody.innerHTML = "";

        if (incidente.length === 0) {
            tbody.innerHTML = `<tr><td colspan="6" style="text-align: center; color: #10b981; padding: 20px;">Nu există niciun incident în așteptare!</td></tr>`;
            return;
        }

        incidente.forEach(inc => {
            const dataFormata = new Date(inc.dataRaportare).toLocaleString("ro-RO");
            tbody.innerHTML += `
                <tr style="border-bottom: 1px solid rgba(255,255,255,0.05);">
                    <td style="padding: 15px;">${dataFormata}</td>
                    <td style="padding: 15px;"><strong>${inc.numeRaportor}</strong></td>
                    <td style="padding: 15px; color: #3b82f6;">${inc.numeLocalitate}</td>
                    <td style="padding: 15px; color: #f59e0b;">${inc.tipInfractiune}</td>
                    <td style="padding: 15px; max-width: 250px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" title="${inc.descriere || '-'}">
                        ${inc.descriere || '-'}
                    </td>
                    <td style="padding: 15px; text-align: center;">
                        <button onclick="aprobaIncident(${inc.id})" style="background: #10b981; border: none; padding: 6px 12px; color: white; border-radius: 6px; cursor: pointer; margin-right: 5px;">
                            <i class="fa-solid fa-check"></i>
                        </button>
                        <button onclick="respingeIncident(${inc.id})" style="background: #ef4444; border: none; padding: 6px 12px; color: white; border-radius: 6px; cursor: pointer;">
                            <i class="fa-solid fa-xmark"></i>
                        </button>
                    </td>
                </tr>
            `;
        });
    } catch (error) {
        tbody.innerHTML = `<tr><td colspan="6" style="text-align: center; color: #ef4444; padding: 20px;">Eroare la conectarea cu serverul!</td></tr>`;
    }
}

// ==========================================
// --- LOGICA NOTIFICĂRI (SIDEBAR + MODAL) ---
// ==========================================
if (localStorage.getItem("user")) {
    setInterval(verificaNotificariNoi, 30000);
    setTimeout(verificaNotificariNoi, 1000);
}

async function verificaNotificariNoi() {
    const userData = localStorage.getItem("user");
    if (!userData) return;
    const user = JSON.parse(userData);

    try {
        const res = await fetch(`${API_NOTIF}/utilizator/${user.id}/necitite`);
        if (res.ok) {
            const necitite = await res.json();
            const badge = document.getElementById("notifBadge");
            if (badge) {
                if (necitite.length > 0) {
                    badge.innerText = necitite.length;
                    badge.style.display = "block";
                } else {
                    badge.style.display = "none";
                }
            }
        }
    } catch (e) { console.error(e); }
}

async function toggleNotificari(event) {
    if (event) event.preventDefault();

    const modal = document.getElementById("notifModal");
    const userData = localStorage.getItem("user");
    if (!userData || !modal) return;
    const user = JSON.parse(userData);

    if (modal.style.display === "block") {
        modal.style.display = "none";
    } else {
        modal.style.display = "block";
        await incarcaToateNotificarile(user.id);
        try {
            await fetch(`${API_NOTIF}/citeste-toate/${user.id}`, { method: 'PUT' });
            verificaNotificariNoi();
        } catch (e) { console.error(e); }
    }
}

async function incarcaToateNotificarile(userId) {
    const container = document.getElementById("listaNotificariContent");
    if (!container) return;

    try {
        const res = await fetch(`${API_NOTIF}/utilizator/${userId}`);
        if (res.ok) {
            const list = await res.json();
            container.innerHTML = "";

            if (list.length === 0) {
                container.innerHTML = '<p style="text-align:center; padding:20px; color:#64748b;">Nu ai nicio notificare încă.</p>';
                return;
            }

            list.forEach(n => {
                const dataStr = n.dataNotificare ? new Date(n.dataNotificare).toLocaleString("ro-RO") : "Recent";
                container.innerHTML += `
                    <div class="notif-item ${n.citit ? '' : 'necitit'}" style="padding: 12px; margin-bottom: 8px; border-radius: 6px; background: rgba(255,255,255,0.05); border-left: 4px solid ${n.citit ? '#3b82f6' : '#ef4444'};">
                        <p style="margin: 0; font-size: 0.9rem; color: #f8fafc;">${n.mesaj}</p>
                        <span style="font-size: 0.75rem; color: #94a3b8; display: block; margin-top: 5px;">
                            <i class="fa-solid fa-clock"></i> ${dataStr}
                        </span>
                    </div>
                `;
            });
        }
    } catch (e) {
        container.innerHTML = "<p style='color: #ef4444;'>Eroare la încărcarea datelor.</p>";
    }
}

// ==========================================
// --- 10. LOGICA POP-UP CONFIRMARE ADMIN ---
// ==========================================
function deschidePopUpConfirmare(config) {
    const modal = document.getElementById("confirmModal");
    if (!modal) return;

    document.getElementById("confirmTitle").innerText = config.titlu;
    document.getElementById("confirmMessage").innerText = config.mesaj;

    const iconDiv = document.getElementById("confirmIcon");
    iconDiv.innerHTML = config.iconHtml;
    iconDiv.style.color = config.culoare;

    const successBtn = document.getElementById("confirmSuccessBtn");
    successBtn.innerText = config.textButon;
    successBtn.style.backgroundColor = config.culoare;

    modal.style.display = "flex";

    successBtn.onclick = async () => {
        modal.style.display = "none";
        showToast("Se procesează...", "info");
        await config.actiuneFinala();
    };

    document.getElementById("confirmCancelBtn").onclick = () => {
        modal.style.display = "none";
    };

    modal.onclick = (e) => {
        if (e.target === modal) modal.style.display = "none";
    };
}

async function aprobaIncident(id) {
    deschidePopUpConfirmare({
        titlu: "Aprobare Incident",
        mesaj: "Ești sigur că vrei să CONFIRMI acest incident? Va apărea pe hartă și cetățenii abonați vor primi notificări.",
        iconHtml: '<i class="fa-solid fa-circle-check"></i>',
        culoare: "#10b981",
        textButon: "Aprobă",
        actiuneFinala: async () => {
            try {
                const response = await fetch(`${API_ADMIN}/incidente/${id}/aproba`, { method: 'PUT' });
                showToast(await response.text(), response.ok ? 'success' : 'error');
                if (response.ok) incarcaIncidenteAdmin();
            } catch (e) { showToast("Eroare de conexiune la server!", "error"); }
        }
    });
}

async function respingeIncident(id) {
    deschidePopUpConfirmare({
        titlu: "Respingere Incident",
        mesaj: "Acest raport va fi marcat ca FALS. Sigur vrei să îl respingi? Nu va fi vizibil pe hartă.",
        iconHtml: '<i class="fa-solid fa-circle-xmark"></i>',
        culoare: "#ef4444",
        textButon: "Respinge",
        actiuneFinala: async () => {
            try {
                const response = await fetch(`${API_ADMIN}/incidente/${id}/respinge`, { method: 'PUT' });
                showToast(await response.text(), response.ok ? 'success' : 'error');
                if (response.ok) incarcaIncidenteAdmin();
            } catch (e) { showToast("Eroare de conexiune la server!", "error"); }
        }
    });
}

// ==========================================
// --- 11. ZONA APLICARE ADMIN (CETĂȚEAN) ---
// ==========================================
async function incarcaPaginaAplicaAdmin() {
    const user = JSON.parse(localStorage.getItem("user"));
    if (!user) return window.location.href = "login.html";

    const boxMesaj = document.getElementById("boxMesajEligibilitate");
    const boxForm = document.getElementById("boxFormularAplicatie");

    try {
        const res = await fetch(`${API_APLICATII}/eligibilitate/${user.id}`);
        const text = await res.text();

        if (res.ok && text === "ELIGIBIL") {
            boxMesaj.style.display = "none";
            boxForm.style.display = "block";
        } else {
            boxMesaj.innerHTML = `<div style="background: rgba(239, 68, 68, 0.1); border-left: 4px solid #ef4444; padding: 20px; border-radius: 8px;"><h3 style="color: #ef4444; margin-top: 0;"><i class="fa-solid fa-triangle-exclamation"></i> Acces Restricționat</h3><p style="color: #334155; margin-bottom: 0; font-weight: 500;">${text}</p></div>`;
            boxMesaj.style.display = "block";
            boxForm.style.display = "none";
        }
    } catch (e) { console.error(e); }
}

async function trimiteAplicatieAdmin() {
    const user = JSON.parse(localStorage.getItem("user"));
    const motivatie = document.getElementById("motivatieAdmin").value;

    if (!motivatie || motivatie.length < 20) {
        return showToast("Te rugăm să scrii o motivație de minim 20 de caractere!", "error");
    }

    try {
        const res = await fetch(`${API_APLICATII}/aplica/${user.id}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ motivatie: motivatie })
        });

        showToast(await res.text(), res.ok ? "success" : "error");

        if (res.ok) {
            document.getElementById("boxFormularAplicatie").style.display = "none";
            document.getElementById("boxMesajEligibilitate").innerHTML = `<div style="background: rgba(16, 185, 129, 0.1); border-left: 4px solid #10b981; padding: 20px; border-radius: 8px;"><h3 style="color: #10b981; margin-top: 0;"><i class="fa-solid fa-check"></i> Aplicație Trimisă</h3><p style="color: #334155; margin-bottom: 0; font-weight: 500;">Cererea ta a fost trimisă cu succes. Urmărește clopoțelul de notificări pentru răspuns!</p></div>`;
            document.getElementById("boxMesajEligibilitate").style.display = "block";
        }
    } catch (e) { showToast("Eroare de conexiune la server!", "error"); }
}

// ==========================================
// --- 12. PANOUL SUPREM (ADMIN SUPREM) ---
// ==========================================
async function incarcaAplicatiiSuprem() {
    const container = document.getElementById("gridAplicatii");
    if (!container) return;

    try {
        const res = await fetch(`${API_APLICATII}/in-asteptare`);
        if (res.ok) {
            const aplicatii = await res.json();
            container.innerHTML = "";

            if (aplicatii.length === 0) {
                container.innerHTML = `<div style="width: 100%; text-align: center; color: #10b981; padding: 40px; background: rgba(16, 185, 129, 0.1); border-radius: 12px; border: 1px solid rgba(16,185,129,0.3);">Nu există nicio cerere în așteptare în acest moment!</div>`;
                return;
            }

            aplicatii.forEach(app => {
                const dataFormata = new Date(app.dataAplicare).toLocaleString("ro-RO");
                let motivatieScurta = app.motivatie.length > 90 ? app.motivatie.substring(0, 90) + "..." : app.motivatie;
                const numeSafe = app.numeUtilizator.replace(/'/g, "\\'");
                const motivatieSafe = app.motivatie.replace(/'/g, "\\'").replace(/"/g, '&quot;');

                container.innerHTML += `
                    <div class="premium-dark-panel"
                         style="flex: 1 1 350px; max-width: 500px; padding: 25px; cursor: pointer; transition: all 0.2s ease; border-top: 4px solid #8b5cf6; display: flex; flex-direction: column; justify-content: space-between; box-sizing: border-box;"
                         onclick="deschideModalReview(${app.id}, '${numeSafe}', '${motivatieSafe}', '${dataFormata}')"
                         onmouseover="this.style.transform='translateY(-5px)'; this.style.boxShadow='0 10px 25px rgba(0,0,0,0.4)';"
                         onmouseout="this.style.transform='translateY(0)'; this.style.boxShadow='none';">

                        <div>
                            <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 15px;">
                                <h3 style="margin: 0; color: #e2e8f0; font-size: 1.25rem;">${app.numeUtilizator}</h3>
                                <span style="font-size: 0.8rem; color: #94a3b8; background: rgba(255,255,255,0.05); padding: 5px 10px; border-radius: 6px;">${dataFormata.split(',')[0]}</span>
                            </div>
                            <p style="color: #cbd5e1; font-size: 0.95rem; font-style: italic; line-height: 1.6; margin: 0;">"${motivatieScurta}"</p>
                        </div>

                        <div style="margin-top: 25px; text-align: right; border-top: 1px solid rgba(255,255,255,0.05); padding-top: 15px;">
                            <span style="color: #8b5cf6; font-size: 0.9rem; font-weight: 600;"><i class="fa-solid fa-magnifying-glass"></i> Deschide cererea</span>
                        </div>
                    </div>
                `;
            });
        }
    } catch (e) {
        container.innerHTML = `<div style="width: 100%; text-align: center; color: #ef4444; padding: 20px;">Eroare la încărcarea datelor de pe server.</div>`;
    }
}

let aplicatieCurentaId = null;

function deschideModalReview(id, nume, motivatie, data) {
    aplicatieCurentaId = id;
    document.getElementById("reviewNume").innerText = nume;
    document.getElementById("reviewData").innerText = data;
    document.getElementById("reviewMotivatie").innerText = motivatie;
    document.getElementById("btnAproba").onclick = () => proceseazaCerere('APROBA');
    document.getElementById("btnRespinge").onclick = () => proceseazaCerere('RESPINGE');
    document.getElementById("reviewModal").style.display = "flex";
}

function inchideModalReview() {
    document.getElementById("reviewModal").style.display = "none";
    aplicatieCurentaId = null;
}

async function proceseazaCerere(actiune) {
    if (!aplicatieCurentaId) return;

    const mesajRaspuns = prompt(`Scrie un mesaj pentru cetățean:\n(Acesta va fi trimis sub formă de notificare)`, `Ai fost ${actiune === 'APROBA' ? 'promovat' : 'respins'}!`);

    if (mesajRaspuns === null) return;
    if (mesajRaspuns.trim() === "") return showToast("Trebuie să introduci un motiv!", "error");

    try {
        const response = await fetch(`${API_APLICATII}/raspunde/${aplicatieCurentaId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ actiune: actiune, mesaj: mesajRaspuns })
        });

        showToast(await response.text(), response.ok ? 'success' : 'error');

        if (response.ok) {
            inchideModalReview();
            incarcaAplicatiiSuprem();
        }
    } catch (e) { showToast("Eroare de conexiune la server!", "error"); }
}

// ==========================================
// --- 13. PREDICȚII AI (PAGINA SEPARATĂ) ---
// ==========================================
let chartPredictiiInstance = null;

async function incarcaDatePredictiiDinamice() {
    const loadingDiv = document.getElementById("loadingPredictii");
    const contentDiv = document.getElementById("continutPredictii");

    if (!loadingDiv || !contentDiv) return;

    try {
        const response = await fetch('http://localhost:8080/api/predictii/dinamic');

        if (response.ok) {
            const datePredictii = await response.json();

            loadingDiv.style.display = "none";
            contentDiv.style.display = "block";

            populeazaTabelPredictii(datePredictii);
            deseneazaGraficPredictii(datePredictii);
        } else {
            loadingDiv.innerHTML = `<h2 style="color: #ef4444;"><i class="fa-solid fa-triangle-exclamation"></i> Eroare la rularea modelelor AI.</h2>`;
        }
    } catch (e) {
        console.error("Eroare rețea:", e);
        loadingDiv.innerHTML = `<h2 style="color: #ef4444;"><i class="fa-solid fa-server"></i> Eroare de conexiune la serverul Java.</h2>`;
    }
}

function populeazaTabelPredictii(date) {
    const tbody = document.getElementById("tabelPredictiiBody");
    if (!tbody) return;
    tbody.innerHTML = "";

    date.forEach(rand => {
        const culoareCoeficient = rand.coeficient_prezis > 100 ? '#ef4444' : (rand.coeficient_prezis > 75 ? '#f59e0b' : '#10b981');

        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td style="font-weight: 600; color: white;">${rand.nume_judet}</td>
            <td style="color: #94a3b8;">${rand.an_vizat}</td>
            <td style="color: ${culoareCoeficient}; font-weight: 700; font-family: monospace; font-size: 1.05rem;">
                ${Number(rand.coeficient_prezis).toFixed(2)}
            </td>
            <td style="color: #64748b; font-size: 0.9rem;">${rand.data_generare}</td>
        `;
        tbody.appendChild(tr);
    });
}

function deseneazaGraficPredictii(date) {
    const ctx = document.getElementById('predictiiChart');
    if (!ctx) return;

    const top10 = date.slice(0, 10);
    const labels = top10.map(d => d.nume_judet);
    const dataValues = top10.map(d => Number(d.coeficient_prezis).toFixed(2));

    if (chartPredictiiInstance) {
        chartPredictiiInstance.destroy();
    }

    chartPredictiiInstance = new Chart(ctx.getContext('2d'), {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Coeficient Prezis',
                data: dataValues,
                backgroundColor: 'rgba(59, 130, 246, 0.7)',
                borderColor: '#3b82f6',
                borderWidth: 1,
                borderRadius: 4
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { display: false } },
            scales: {
                y: { ticks: { color: '#cbd5e1' }, grid: { color: 'rgba(255,255,255,0.05)' } },
                x: { ticks: { color: '#cbd5e1' }, grid: { display: false } }
            }
        }
    });
}

// ==========================================
// --- 14. EXPORT DATE (PAGINA SEPARATĂ) ---
// ==========================================
function declanseazaExport() {
    const format = document.getElementById("exportFormat").value;
    const an = document.getElementById("exportAn").value;

    if (!format) {
        return showToast("Te rugăm să selectezi un format!", "error");
    }

    showToast("Se pregătește fișierul...", "info");

    // Construim link-ul către ruta ta nouă (folosind /api/exporturi)
    const url = `http://localhost:8080/api/exporturi/descarca?format=${format}&an=${an}`;

    // Deschidem link-ul. Browserul va salva fișierul imediat.
    window.open(url, "_blank");
}
// ==========================================
// --- 15. GESTIUNE UTILIZATORI (ADMIN SUPREM) ---
// ==========================================
async function incarcaListaUtilizatori() {
    const tbody = document.getElementById("tabelUtilizatoriBody");
    if (!tbody) return;

    try {
        const response = await fetch(API_UTILIZATORI);
        if (response.ok) {
            const utilizatori = await response.json();
            tbody.innerHTML = "";

            if (utilizatori.length === 0) {
                tbody.innerHTML = `<tr><td colspan="4" style="text-align: center; color: var(--text-muted); padding: 20px;">Nu a fost găsit niciun utilizator.</td></tr>`;
                return;
            }

            utilizatori.forEach(user => {
                // Nu edităm Adminul Suprem
                if (user.rol === "ADMIN_SUPREM") return;

                let butoane = "";
                let badgeRol = "";

                // Formatăm data de înregistrare (ex: 25.04.2026)
                const dataFormatata = user.dataInregistrare
                    ? new Date(user.dataInregistrare).toLocaleDateString("ro-RO")
                    : "N/A";

                if (user.rol === "CETATEAN") {
                    badgeRol = `<span class="badge" style="background-color: #3b82f6; color: white; padding: 4px 8px; border-radius: 4px;">Cetățean</span>`;
                    butoane = `<button onclick="schimbaRol(${user.id}, 'ADMIN')" style="background: #10b981; color: white; border: none; padding: 6px 12px; border-radius: 6px; cursor: pointer; font-size: 0.9rem; width: 160px;"><i class="fa-solid fa-arrow-up"></i> Promovează Admin</button>`;
                } else if (user.rol === "ADMIN") {
                    badgeRol = `<span class="badge" style="background-color: #f59e0b; color: white; padding: 4px 8px; border-radius: 4px;">Admin</span>`;
                    butoane = `<button onclick="schimbaRol(${user.id}, 'CETATEAN')" style="background: #ef4444; color: white; border: none; padding: 6px 12px; border-radius: 6px; cursor: pointer; font-size: 0.9rem; width: 160px;"><i class="fa-solid fa-arrow-down"></i> Revocă Cetățean</button>`;
                }

                tbody.innerHTML += `
                    <tr style="border-bottom: 1px solid rgba(255,255,255,0.05);">
                        <td style="padding: 15px; font-weight: 500;">${user.numeComplet}</td>
                        <td style="padding: 15px; color: #94a3b8;">${dataFormatata}</td> <td style="padding: 15px;">${badgeRol}</td>
                        <td style="padding: 15px; text-align: right;">${butoane}</td>
                    </tr>
                `;
            });
        }
    } catch (e) {
        console.error("Eroare la încărcarea utilizatorilor:", e);
        tbody.innerHTML = `<tr><td colspan="4" style="text-align: center; color: #ef4444; padding: 20px;">Eroare la conectarea cu serverul!</td></tr>`;
    }
}

async function schimbaRol(userId, nouRol) {
    // 1. Pregătim aspectul vizual al ferestrei în funcție de butonul apăsat
    let configPopUp = {};

    if (nouRol === 'ADMIN') {
        configPopUp = {
            titlu: "Promovare la Admin",
            mesaj: "Ești sigur că vrei să promovezi acest utilizator? Va avea acces la panoul de administrare pentru validarea incidentelor de pe hartă.",
            iconHtml: '<i class="fa-solid fa-shield-halved fa-beat-fade"></i>',
            culoare: "#10b981", // Verde
            textButon: "Da, Promovează"
        };
    } else {
        configPopUp = {
            titlu: "Revocare Acces",
            mesaj: "Ești sigur că vrei să îi retragi drepturile de Admin? Va redeveni un simplu cetățean și va pierde accesul la panou.",
            iconHtml: '<i class="fa-solid fa-user-minus"></i>',
            culoare: "#ef4444", // Roșu
            textButon: "Da, Revocă"
        };
    }

    // 2. Definim ce se întâmplă DUPĂ ce Adminul Suprem dă click pe confirmare
    configPopUp.actiuneFinala = async () => {
        try {
            const response = await fetch(`${API_UTILIZATORI}/${userId}/schimba-rol?nouRol=${nouRol}`, {
                method: 'PUT'
            });

            if (response.ok) {
                showToast(await response.text(), 'success');
                incarcaListaUtilizatori(); // Reîncărcăm tabelul
            } else {
                showToast(await response.text(), 'error');
            }
        } catch (e) {
            showToast("Eroare de conexiune la server!", "error");
        }
    };

    // 3. Afișăm pop-up-ul frumos pe ecran apelând funcția generală deja existentă în codul tău
    deschidePopUpConfirmare(configPopUp);
}