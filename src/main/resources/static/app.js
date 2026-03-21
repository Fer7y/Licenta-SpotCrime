// ==========================================
// --- CONSTANTE API ---
// ==========================================
const API_URL = "http://localhost:8080/api/auth";
const API_INCIDENTE = "http://localhost:8080/api/incidente";
const API_ISTORIC = "http://localhost:8080/api/istoric";
const API_ABONAMENTE = "http://localhost:8080/api/abonamente"; // NOU

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

    const loginItem = document.getElementById("loginMenuItem");
    const logoutItem = document.getElementById("logoutMenuItem");
    const welcomeMessage = document.getElementById("welcomeMessage");
    const profileHeaderBtn = document.getElementById("profileHeaderBtn");
    const profileHeaderText = document.getElementById("profileHeaderText");

    if (userData) {
        const user = JSON.parse(userData);
        if(loginItem) loginItem.style.display = "none";
        if(logoutItem) logoutItem.style.display = "block";
        if(profileHeaderText) profileHeaderText.innerText = "Contul Meu";

        if(profileHeaderBtn) {
            profileHeaderBtn.href = "profil.html";
            profileHeaderBtn.onclick = null;
        }
    } else {
        if(profileHeaderText) profileHeaderText.innerText = "Guest";
        if(profileHeaderBtn) {
            profileHeaderBtn.href = "#";
            profileHeaderBtn.onclick = (e) => e.preventDefault();
        }
    }

    // Dacă suntem pe profil, încărcăm tot (inclusiv abonamentele)
    if (document.getElementById("profNume")) {
        incarcaDateProfil();
    }

    // Dacă suntem pe dashboard
    if (document.getElementById("globalYearSelect")) {
        incarcaDashboard();
        populeazaDropdownJudete();
    }
});

// ==========================================
// --- 2. AUTENTIFICARE ---
// ==========================================
function logout() {
    localStorage.removeItem("user");
    window.location.href = "login.html";
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
// --- 3. PROFIL & ALERTE (NOU) ---
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

    // Încărcăm statisticile
    try {
        const response = await fetch(`${API_INCIDENTE}/utilizator/${user.id}`);
        if (response.ok) {
            const incidente = await response.json();
            document.getElementById("statTotal").innerText = incidente.length;
            document.getElementById("statConfirmate").innerText = incidente.filter(i => i.status === 'APROBAT').length;
            document.getElementById("statFalse").innerText = incidente.filter(i => i.status === 'RESPINS').length;
        }
    } catch (err) { console.error(err); }

    // ÎNCĂRCĂM ABONAMENTELE LA ALERTE (NOU)
    incarcaAbonamente(user.id);
}

// --- LOGICA PENTRU AUTO-COMPLETE ȘI ABONAMENTE ---

// Aducem orașele la care e abonat și le punem sub formă de badges
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

// Funcție declanșată la fiecare literă tastată (Search)
let searchTimeout;
async function cautaOras(text) {
    const resultsBox = document.getElementById("searchResults");

    // Dacă a scris prea puțin, ascundem cutia
    if (!text || text.length < 2) {
        resultsBox.style.display = "none";
        return;
    }

    // Curățăm timer-ul anterior (ca să nu facă spam la server)
    clearTimeout(searchTimeout);

    // Așteptăm 300ms după ultima tastare
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

// Când dă click pe un oraș din listă
async function adaugaAbonament(localitateId) {
    const userData = JSON.parse(localStorage.getItem("user"));
    if (!userData) return;

    // Ascundem rezultatele și golim căsuța de search
    document.getElementById("searchResults").style.display = "none";
    document.getElementById("searchOras").value = "";

    try {
        const response = await fetch(`${API_ABONAMENTE}/adauga?userId=${userData.id}&localitateId=${localitateId}`, {
            method: 'POST'
        });

        if (response.ok) {
            showToast(await response.text(), "success");
            incarcaAbonamente(userData.id); // Reîncărcăm lista de badge-uri
        } else {
            showToast(await response.text(), "error"); // Eroare (ex: Ești deja abonat)
        }
    } catch (e) { showToast("Eroare de conexiune!", "error"); }
}

// Când dă click pe 'X' de pe un badge
async function stergeAbonament(abonamentId) {
    const userData = JSON.parse(localStorage.getItem("user"));
    if (!userData) return;

    try {
        const response = await fetch(`${API_ABONAMENTE}/sterge/${abonamentId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showToast(await response.text(), "success");
            incarcaAbonamente(userData.id); // Reîncărcăm lista de badge-uri
        } else {
            showToast("Eroare la ștergere", "error");
        }
    } catch (e) { showToast("Eroare de conexiune!", "error"); }
}

// Ascunde auto-complete-ul dacă utilizatorul dă click în afara lui
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
    const anPrecedent = anCurent - 1;

    try {
        const resCurent = await fetch(`${API_ISTORIC}/an/${anCurent}`);
        const resPrecedent = await fetch(`${API_ISTORIC}/an/${anPrecedent}`);

        if (resCurent.ok) {
            const dateCurente = await resCurent.json();
            if (!dateCurente || dateCurente.length === 0) return;

            const datePrecedente = resPrecedent.ok ? await resPrecedent.json() : [];
            deseneazaTopBottom(dateCurente);
            populeazaTabel(dateCurente, datePrecedente);
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
// ==========================================
// --- 6. ZONA HARTĂ INTERACTIVĂ (LEAFLET) ---
// ==========================================

let mapInstance = null;
let geoJsonLayer = null;
let markersLayer = null;

document.addEventListener("DOMContentLoaded", () => {
    if (document.getElementById("map")) {
        console.log("[DEBUG HARTĂ] Pagina de hartă detectată. Pornim...");
        initializeazaHarta();
        incarcaDateHarta();
    }
});

function initializeazaHarta() {
    try {
        mapInstance = L.map('map').setView([45.9000, 24.9668], 7);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors',
            maxZoom: 18,
        }).addTo(mapInstance);
        markersLayer = L.layerGroup().addTo(mapInstance);
        console.log("[DEBUG HARTĂ] Harta de bază a fost încărcată cu succes.");
    } catch (e) {
        console.error("[DEBUG HARTĂ] Eroare la inițializarea hărții Leaflet:", e);
    }
}

async function incarcaDateHarta() {
    const anSelectat = document.getElementById("hartaYearSelect").value;
    console.log(`[DEBUG HARTĂ] Cerem datele pentru anul: ${anSelectat}`);

    let dateCoeficienti = [];

    // 1. Aducem Coeficienții din Backend-ul tău
    try {
        const resCoef = await fetch(`${API_ISTORIC}/an/${anSelectat}`);
        if(resCoef.ok) {
            dateCoeficienti = await resCoef.json();
            console.log("[DEBUG HARTĂ] Coeficienți primiți din BD:", dateCoeficienti.length, "județe");
        }
    } catch(e) { console.error("[DEBUG HARTĂ] Backend-ul nu răspunde (Istoric):", e); }

    // 2. Aducem granițele României (GeoJSON - Link Sigur)
    try {
        console.log("[DEBUG HARTĂ] Se descarcă granițele României...");
        const resGeo = await fetch("https://raw.githubusercontent.com/bumbu/romania-geojson/master/romania-counties.geojson");
        if(resGeo.ok) {
            const geoData = await resGeo.json();
            console.log("[DEBUG HARTĂ] Granițele au fost descărcate! Desenăm poligoanele...");
            deseneazaPoligoane(geoData, dateCoeficienti);
        } else {
            console.error("[DEBUG HARTĂ] Eroare 404: Link-ul GeoJSON este picat.");
        }
    } catch(e) { console.error("[DEBUG HARTĂ] Eroare de rețea la GeoJSON:", e); }

    // 3. Aducem Incidentele Aprobate + FILTRARE PE AN
    try {
        console.log("[DEBUG HARTĂ] Cerem incidentele aprobate de la Backend...");
        const resIncidente = await fetch(`${API_INCIDENTE}/aprobate`);
        if (resIncidente.ok) {
            const incidenteTotiAnii = await resIncidente.json();

            // FILTRARE: Păstrăm doar incidentele care s-au întâmplat în anul selectat!
            const incidenteFiltrate = incidenteTotiAnii.filter(inc => {
                if (!inc.dataRaportare) return true; // Dacă din greșeală nu are dată, îl lăsăm
                const anIncident = new Date(inc.dataRaportare).getFullYear();
                return anIncident === parseInt(anSelectat);
            });

            console.log(`[DEBUG HARTĂ] Din totalul de ${incidenteTotiAnii.length}, am păstrat ${incidenteFiltrate.length} pentru anul ${anSelectat}.`);
            punePionezePeHarta(incidenteFiltrate);
        } else {
            console.error("[DEBUG HARTĂ] Endpoint-ul /aprobate a returnat o eroare.");
        }
    } catch (e) { console.error("[DEBUG HARTĂ] Eroare la aducerea incidentelor:", e); }
}

function normalizareNume(nume) {
    if (!nume) return "";
    return nume.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase();
}

function deseneazaPoligoane(geoData, coeficientiBD) {
    if (geoJsonLayer) { mapInstance.removeLayer(geoJsonLayer); }

    geoJsonLayer = L.geoJSON(geoData, {
        style: function (feature) {
            const numeGeoOriginal = feature.properties.NAME_1 || feature.properties.name || "";
            const numeGeo = normalizareNume(numeGeoOriginal);

            const judetBD = coeficientiBD.find(j => normalizareNume(j.numeJudet) === numeGeo);

            let culoare = '#94a3b8'; // Gri default
            if (judetBD) {
                if (judetBD.domeniuIncadrare === 'RIDICAT') culoare = '#ef4444';
                else if (judetBD.domeniuIncadrare === 'MEDIU') culoare = '#f59e0b';
                else if (judetBD.domeniuIncadrare === 'SCAZUT') culoare = '#10b981';
            }

            return {
                fillColor: culoare,
                weight: 2,
                opacity: 1,
                color: 'white',
                dashArray: '3',
                fillOpacity: 0.5
            };
        },
        onEachFeature: function (feature, layer) {
            const numeGeoOriginal = feature.properties.NAME_1 || feature.properties.name || "";
            const numeGeo = normalizareNume(numeGeoOriginal);
            const judetBD = coeficientiBD.find(j => normalizareNume(j.numeJudet) === numeGeo);

            if (judetBD) {
                layer.bindTooltip(`<strong>Județul ${judetBD.numeJudet}</strong><br>Coeficient: ${judetBD.coeficient}<br>Risc: ${judetBD.domeniuIncadrare}`, { sticky: true });
            } else {
                layer.bindTooltip(`<strong>Județul ${numeGeoOriginal}</strong><br>Nu există date`, { sticky: true });
            }
        }
    }).addTo(mapInstance);
}

function punePionezePeHarta(incidente) {
    markersLayer.clearLayers();
    let adaugate = 0;

    incidente.forEach(inc => {
        if (inc.latitudine && inc.longitudine) {
            const dataInc = new Date(inc.dataRaportare).toLocaleDateString("ro-RO", {
                year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit'
            });

            const popupContinut = `
                <div class="popup-custom">
                    <h3>${inc.tipInfractiune || 'Incident'}</h3>
                    <p>${inc.descriere || 'Fără descriere'}</p>
                    <span class="data-incident"><i class="fa-solid fa-clock"></i> ${dataInc}</span>
                </div>
            `;

            const marker = L.marker([inc.latitudine, inc.longitudine]).bindPopup(popupContinut);
            markersLayer.addLayer(marker);
            adaugate++;
        }
    });
    console.log(`[DEBUG HARTĂ] Am pus cu succes ${adaugate} pioneze pe hartă.`);
}

// ==========================================
// --- 7. ZONA RAPORTARE INCIDENT (FORMULAR + MINI-HARTĂ) ---
// ==========================================

let miniMapInstance = null;
let rapMarker = null;

document.addEventListener("DOMContentLoaded", () => {
    if (document.getElementById("miniMap")) {
        initMiniMap();
    }
});

function initMiniMap() {
    // Setăm harta pe România
    miniMapInstance = L.map('miniMap').setView([45.9000, 24.9668], 6);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap'
    }).addTo(miniMapInstance);

    // Când dă click, luăm coordonatele și punem o pioneză roșie
    miniMapInstance.on('click', function(e) {
        const lat = e.latlng.lat;
        const lng = e.latlng.lng;

        document.getElementById('rapLat').value = lat;
        document.getElementById('rapLng').value = lng;

        if (rapMarker) {
            miniMapInstance.removeLayer(rapMarker);
        }

        const redIcon = new L.Icon({
            iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
            shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
            iconSize: [25, 41], iconAnchor: [12, 41], popupAnchor: [1, -34], shadowSize: [41, 41]
        });

        rapMarker = L.marker([lat, lng], {icon: redIcon}).addTo(miniMapInstance);
    });
}

// Căutare oraș pentru raportare
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

// Funcția de trimitere finală
async function trimiteRaport() {
    const userData = JSON.parse(localStorage.getItem("user"));
    if (!userData) {
        showToast("Trebuie să fii autentificat pentru a raporta un incident!", "error");
        setTimeout(() => window.location.href = "login.html", 2000);
        return;
    }

    const idLocalitate = document.getElementById("rapLocalitateId").value;
    const tipInfractiune = document.getElementById("rapTip").value;
    const descriere = document.getElementById("rapDescriere").value;
    const lat = document.getElementById("rapLat").value;
    const lng = document.getElementById("rapLng").value;

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

            // Golim formularul după succes
            document.getElementById("rapTip").value = "";
            document.getElementById("rapDescriere").value = "";
            document.getElementById("rapLocalitateId").value = "";
            document.getElementById("rapOrasSelectatText").style.display = "none";
            if (rapMarker) miniMapInstance.removeLayer(rapMarker);
            document.getElementById("rapLat").value = "";
            document.getElementById("rapLng").value = "";
        } else {
            showToast(await response.text(), "error");
        }
    } catch (e) {
        showToast("Eroare de conexiune la server!", "error");
    }
}