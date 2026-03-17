# Sentinel 9.7.2 Deep-Trace

**Sentinel 9.7.2** ist ein fortschrittliches Android-Monitoring-Tool zur Analyse systemischer Marktstabilität. Basierend auf dem Deep-Trace-Protokoll kombiniert die App makroökonomische Liquiditätsindikatoren mit der relativen Stärke individueller Assets (z. B. NVTS), um präzise Risk-Scores und Hedge-Indikationen zu generieren.

## 🚀 Kern-Features

- **Triple-Layer Analyse:** - **Layer 0 (Liquidity):** Überwachung des USD/JPY (Carry-Trade) und Fed Repo-Injektionen.
  - **Layer 1 (Macro):** Einbeziehung von VIX und offiziellen Treasury-Daten.
  - **Layer 2 (Asset):** Relative Stärkemessung und Alpha-Generierung.
- **Dynamisches Monitoring:** Umschaltbare Intervalle zwischen strategischer (Weekly) und taktischer (4h) Beobachtung.
- **Interaktives Dashboard:** Visuelle Aufbereitung des Sentinel-Scores (1.0 - 10.0).
- **Abstrahierter Daten-Layer:** Nahtloser Wechsel zwischen simulierten Szenarien (Mock-Daten) und Echtzeit-Marktdaten.

## 🏗️ Projekt-Architektur

Die App folgt dem **MVVM-Muster** (Model-View-ViewModel) und nutzt modernste Android-Technologien:

* **UI:** Jetpack Compose (deklarative GUI)
* **Sprache:** Kotlin (Coroutines für asynchrone Berechnungen)
* **Architektur:** Clean Architecture Ansätze mit Repository-Pattern
* **Testing:** JUnit & Truth für die Validierung der Kern-Logik

## 📂 Struktur

```text
com.sentinel.deeptrace/
├── core/         # Logik-Engine & Schwellenwerte
├── data/         # Datenbeschaffung (API & Lokaler Cache)
├── domain/       # UseCases (Business Logic)
└── ui/           # Dashboard & UI-Komponenten
