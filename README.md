# OO
# UML Editor 程式架構與類別詳解 (Class Details)

這份文件為你整理了每一個 Java 檔案在程式碼層級的具體架構。包含裡面存了哪些變數（資料）以及負責哪些方法（行為）。如果你被問到「這個類別裡面寫了什麼？」，你可以直接參考這份文件。

---

## 一、主程式與畫布 (View / Controller)

### 1. `MainScreen.java`
* **角色：** 程式入口（包含 `main` 方法），繼承自 `JFrame`。負責排版所有的按鈕和選單。
* **主要變數：**
  * 各種 `JButton` (selectButton, rectButton 等)。
  * `Canvas canvas`: 畫布物件，用來將按鍵的操作傳遞給畫布。
* **核心邏輯：**
  * 在 UI 上綁定 `ActionListener` 與 `MouseListener`。
  * 點擊形狀或線條按鈕時，呼叫 `canvas.setCurrentMode(new XxxMode())` 切換畫布狀態。
  * 處理 "Edit -> Label" 的邏輯，跳出 `JColorChooser` 讓使用者選顏色與填寫文字。

### 2. `Canvas.java`
* **角色：** 繪圖核心與事件分發中心，繼承自 `JPanel`。
* **主要變數：**
  * `ArrayList<GraphObject> objects`: 儲存畫布上「所有的物件與線條」。
  * `Mode currentMode`: 當下正在使用的工具模式。
  * 各種拖曳與預覽的座標變數 (`previewStart`, `selectionStart` 等)。
* **核心邏輯：**
  * **繪圖 `paintComponent(Graphics g)`**: 用 `for` 迴圈呼叫清單中每一個 `GraphObject` 的 `draw(g)` 方法。
  * **滑鼠事件代理**: 當在畫布上點擊或拖曳滑鼠時，將 `MouseEvent` 原封不動地交給 `currentMode` 去處理。

---

## 二、圖形基礎與實作 (Model)

### 1. `GraphObject.java` (抽象類別)
* **角色：** 所有畫面上圖形與線條的最高層父類別。
* **主要變數：**
  * `boolean selected`: 是否正在被選取。
  * `String label`: 圖形上的文字。
  * `Color fillColor`: 圖形的填充顏色。
* **核心方法：**
  * `abstract void draw(Graphics g)`: 強制子類別必須實作自己的畫法。
  * `abstract boolean contains(Point p)`: 強制子類別計算滑鼠有沒有點在自己身上。
  * `abstract void move(int dx, int dy)`: 移動邏輯。
  * 共同實作的 `Setter/Getter`: 包含設定顏色、標籤文字等。

### 2. `RectObject.java` & `OvalObject.java`
* **角色：** 具體的基本圖狀，繼承自 `GraphObject`。
* **主要變數：**
  * `int x, y, width, height`: 形狀的座標與大小。
* **核心邏輯：**
  * `draw()`: 設定顏色後呼叫 `g.fillRect` / `g.fillOval` 等 Java AWT 繪圖方法。另外如果 `selected` 為 true，會把四面八方的 `Port` 畫出來。
  * `getPorts()`: 產生並回傳 8 個方位 (上下左右與四個角) 的 `Port` 陣列，供連線使用。
  * `resize()`: 詳細計算若滑鼠拉動某個 Port 時，`x, y, width, height` 該如何增減（並設定最少縮放限制 `MIN_SIZE`）。

### 3. `CompositeObject.java`
* **角色：** 群組物件，實作 Composite Pattern，繼承自 `GraphObject`。
* **主要變數：**
  * `List<GraphObject> children`: 儲存這個群組包裝了哪些子圖形。
* **核心邏輯：**
  * `draw()` / `move()`: 利用 `for` 迴圈遞迴，呼叫所有 `children` 執行相同的操作。
  * `getBounds()`: 計算出一個足以包覆所有子圖形的「超大矩形邊界」，用來畫外框與檢查滑鼠選取。

---

## 三、線條與節點系統

### 1. `Port.java`
* **角色：** 代表圖形邊緣可以被連線的「端點」。
* **主要變數：**
  * `GraphObject owner`: 記住這個 Port 是屬於哪一個圖形的。
  * `PortPosition position`: 記住這個 Port 在形狀上的方位（利用 `enum`）。
* **核心邏輯：**
  * `contains(Point p)`: 判斷滑鼠是否精準點擊在小黑方塊的座標範圍內。

### 2. `LinkObject.java` (抽象類別)
* **角色：** 所有連線的基底，繼承自 `GraphObject`。
* **主要變數：**
  * `Port startPort`: 線條出發的節點。
  * `Port endPort`: 線條連接的目標節點。
* **核心邏輯：**
  * 在建立時強制綁定兩個點。因為繼承了 `GraphObject`，它一樣會被放進 `Canvas.objects` 清單中維護。

### 3. 三條具體的線 (`AssociationLine`, `GeneralizationLine`, `CompositionLine`)
* **角色：** 特定的線條樣式。
* **核心邏輯：**
  * `draw(Graphics g)`: 分別去問 `startPort` 與 `endPort` 現在最新的座標在哪裡 (`getPosition()`)，然後利用 `g.drawLine` 將兩點連起來。同時再依據各自的定義，在終點處用線段畫出專屬的箭頭設計。

---

## 四、事件模式系統 (State Pattern)

### 1. `Mode.java` (介面)
* **角色：** 規定所有的工具模式都要可以處理 `mousePressed`, `mouseDragged`, `mouseReleased` 這三個關鍵動作。

### 2. `SelectMode.java`
* **角色：** 功能最多最複雜的模式，專心處理「選取操作的各種可能性」。
* **主要變數：**
  * `GraphObject selectedObject`: 目前被點選的單一圖形。
  * 以及一堆標記正在做什麼的 boolean，例如 `isDraggingObject`, `isSelectingArea`, `isResizing`。
* **核心邏輯：**
  * **按壓 (Pressed):** 依序檢查點到了 Port(縮放)、點到了圖形(準備移動圖形)、或點到了空白處(準備框選)。
  * **拖曳 (Dragged):** 依據按壓時的判斷，更新圖形座標 `move()`，或者是去呼叫 `resize()` 調整大小。
  * **放開 (Released):** 如果是框選模式，計算框選範圍 `SelectionRectangle`，並判斷哪些圖形的範圍有落在框框內，將它們設定為 selected。

### 3. 單純產生圖形的 Modes (`RectMode`, `OvalMode`)
* **角色：** 在滑鼠畫面上拖曳時產生圖形的邏輯負責人。
* **核心邏輯：**記錄滑鼠游標拖曳起點至終點的距離，在 `mouseReleased` 時 `new` 一個對應的物件丟進 `Canvas` 裡面。

### 4. 畫線的 Modes (`AssociationMode`, `GeneralizationMode` 等)
* **角色：** 繪製不同連接線的負責人。
* **主要變數：**
  * `Port startPort`: 記錄按下滑鼠那一刻，停在哪個 Port 上面。
* **核心邏輯：**
  * 拖曳時，指示畫布畫出一條跟著滑鼠游標跑的虛線。
  * 滑鼠放開時，判斷滑鼠目前的點是否有掉在另外一個 `Port` 上。如果「起終點皆成立」，就立刻實例化一條新的連線丟進 `Canvas` 內。
