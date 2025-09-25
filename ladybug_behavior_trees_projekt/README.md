# Behavior Tree Käferspiel

Dieses Projekt ist im Rahmen der KIT-Lehrveranstaltung Programmieren von Grund auf von mir entwickelt worden.  
Es handelt sich um ein Spiel mit einem zweidimensionalen Spielfeld, auf dem sich verschiedene Objekte sowie Marienkäfer befinden.  
Die Marienkäfer werden durch Behavior Trees gesteuert.

Sowohl das Spielfeld als auch die Behavior Trees werden aus externen Dateien eingelesen, geparst und validiert.  
Bei der Ausführung werden alle Behavior Trees nacheinander traversiert,bis jeweils eine Aktion pro Tree ausgeführt wurde.  
Danach stoppt die Ausführung automatisch und der nächste CLI-Befehl kann ausgeführt werden.  
Zusätzlich existieren Befehle, mit denen die Behavior Trees während der Laufzeit manipuliert werden können.

---

## 🎯 Projektziele
- Umsetzung einer vollständigen Anwendung im Rahmen der Lehrveranstaltung Programmieren
- Parser und Validator für Behavior Trees in Mermaid-Syntax
- Spielfeld-Parsing aus Datei
- Steuerung mehrerer Marienkäfer über interaktive CLI-Befehle
- Saubere objektorientierte Modellierung und Clean Code

---

## 📂 Beispieldateien
- [exampleBoard.txt](./input/boards/exampleBoard.txt) – Beispiel-Spielfeld
- [firstExampleTree.txt](./input/trees/firstExampleTree.txt) – erster Beispiel-Tree
- [secondExampleTree.txt](./input/trees/secondExampleTree.txt) – zweiter Beispiel-Tree
- [exampleSession.txt](./input/exampleSession.txt) – Beispiel-Interaktion (kann als Redirect Input genutzt werden)

---

## 🛠 Funktionen
- Parsing von Behavior Trees
    - Knotentypen: *Fallback, Sequence, Parallel, Condition, Action*
- Laden und Validieren von Spielfeldern aus Datei
- Steuerung mehrerer Käfer gleichzeitig
- Traversierung und Ausführung der Trees im Rundlaufverfahren (nacheinander, genau eine Aktion pro Tree)
- Manipulation der Behavior Trees über CLI-Befehle
- Interaktive Steuerung über Befehle

---

## 💻 CLI-Befehle
- `quit` – Programm beenden
- `load board <path>` – lädt ein Spielfeld aus Datei
- `load trees <path...>` – lädt ein oder mehrere Behavior Trees aus Datei
- `list ladybugs` – listet IDs aller Marienkäfer im Spielfeld
- `print position <ladybug>` – gibt die Position eines Käfers aus
- `head <ladybug>` – zeigt den aktuellen Knoten im Tree eines Käfers
- `reset tree <ladybug>` – setzt den Behavior Tree eines Käfers zurück
- `jump to <ladybug> <id>` – setzt den aktuellen Knoten auf eine bestimmte ID
- `add sibling <ladybug> <id> <node>` – fügt einem Tree-Knoten ein Geschwister-Element hinzu
- `next action` – traversiert die Behavior Trees und führt je Käfer eine Aktion aus

---

## ▶️ Automatisches Beispiel (Redirect Input)
Mit der Datei [exampleSession.txt](./input/exampleSession.txt)  
kann eine komplette Beispiel-Interaktion automatisch durchlaufen werden
