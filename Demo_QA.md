# UML Editor 期末 Demo 現場問答準備指引 (Q&A Guide)

本文件整理了 10 題助教在 Demo 現場最可能隨機詢問的物件導向設計與程式碼實作問題，並附上詳細解答與程式碼對照，幫助您順利通過 Demo。

---

## 💡 第一部分：設計模式與物件導向設計 (OOP & Design Patterns) - 佔 50% 分數重點

### Q1：你的專案中使用了哪些設計模式 (Design Patterns)？請具體說明它們是如何實作的？
*   **回答**：
    我們主要使用了兩種經典的設計模式：
    1.  **狀態模式 (State Pattern)**：
        *   **目的**：將畫布 `Canvas` 處理滑鼠點擊與拖曳的行為解耦，避免在 Canvas 內部寫大量的 `if-else` 或 `switch-case` 來判斷當前模式。
        *   **實作**：定義了 [Mode.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/Mode.java) 介面，並讓 `SelectMode`、`AssociationMode`、`GeneralizationMode` 和 `CompositionMode` 實作該介面。畫布 [Canvas.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/Canvas.java) 持有當前的 `currentMode`，並在滑鼠事件觸發時委派（Delegate）給 `currentMode` 去執行。
    2.  **組合模式 (Composite Pattern)**：
        *   **目的**：讓單一基本圖形（如 Rect, Oval）與群組物件（Composite）能以相同的方式被處理，達成「部分-整體」的階層結構。
        *   **實作**：[CompositeObject.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/CompositeObject.java) 繼承自 `GraphObject`，同時內部包含一個 `List<GraphObject> children` 來存放被群組的子物件。不論是移動（`move`）、繪製（`draw`）還是選取，都可以對這個 Group 呼叫相同的方法，它會自動遍歷並調用所有子物件的對應方法。

---

### Q2：為什麼在設計滑鼠模式時要使用狀態模式 (State Pattern)？如果不用它，程式碼會變成怎樣？
*   **回答**：
    *   **使用狀態模式的好處**：符合物件導向的 **單一職責原則 (SRP)** 與 **開放封閉原則 (OCP)**。如果未來要新增一種新模式（例如「刪除模式」或「動態連線模式」），我們只需要新增一個實作 `Mode` 介面的類別，完全不需要修改 `Canvas` 既有的滑鼠事件處理程式碼。
    *   **如果不使用狀態模式**：`Canvas` 的 `mousePressed`、`mouseDragged`、`mouseReleased` 方法內將會充斥著大量的 `if (mode == SELECT)`、`else if (mode == ASSOC)` ... 的判斷式。一旦要修改某個模式的滑鼠拖曳細節，極容易不小心改壞其他模式的邏輯，造成程式碼臃腫且難以維護。

---

### Q3：你的群組 (Group) 功能是如何利用組合模式 (Composite Pattern) 實作的？
*   **回答**：
    *   在 [CompositeObject.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/CompositeObject.java) 中，我們讓它繼承 `GraphObject`，並實作抽象方法：
        *   **`move(int dx, int dy)`**：遍歷 `children` 清單，對每個子物件呼叫 `child.move(dx, dy)`，從而使群組內的所有圖形同步移動。
        *   **`draw(Graphics g)`**：遍歷 `children` 清單，呼叫 `child.draw(g)` 來繪製所有子物件；同時，如果該群組被選取或滑鼠懸停（Hovered），則根據所有子物件的邊界計算出一個涵蓋全部圖形的最大外框，並將外框繪製出來。
        *   **`contains(Point p)`**：只要有任何一個子物件包含滑鼠點擊點 `p`，即代表該群組被選中。
    *   在 [Canvas.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/Canvas.java) 中，`groupSelectedObjects()` 會將被選取的複數物件移出畫布的 `objects` 清單，打包進新的 `CompositeObject` 後，再將此 `CompositeObject` 加入 `objects` 清單。解群組（Ungroup）則是反向操作。

---

## 💡 第二部分：程式碼與實作細節 (Implementation Details) - 佔 20% + 15% 分數重點

### Q4：在選取重疊的物件時，你是如何決定哪一個物件在最上層，且最優先被點選？(Z-Order 深度管理)
*   **回答**：
    *   我們的 [Canvas.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/Canvas.java) 使用 `ArrayList<GraphObject> objects` 來存放畫布上的所有圖形。
    *   **繪製順序 (Render Order)**：列表頭部的物件最先畫，尾端的物件最後畫（所以尾端物件會疊在最上層）。
    *   **點擊判定 (Hit Test)**：在 `findObjectAt(Point p)` 中，我們採用**從後往前（Reverse order）**的遍歷方式（從 `objects.size() - 1` 開始遞減到 `0`）。因此，疊在最上層（陣列尾端）的物件會最先被偵測到點擊。
    *   **深度更新 (Z-Order Update)**：當我們在 `SelectMode` 中點擊某個物件時，會觸發 `canvas.bringToFront(selectedObject)`。這個方法會把該物件從 `objects` 串列中移除，並重新 `add` 到串列的最尾端，這樣它在重繪時就會跑到最上層，且下次點擊重疊區域時會優先選中它。

---

### Q5：滑鼠懸停顯示連接點 (Hover Ports) 的功能，程式碼具體是怎麼實現的？
*   **回答**：
    1.  **狀態定義**：我們在基底類別 [GraphObject.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/GraphObject.java) 中新增了 `protected boolean hovered` 屬性與其 getter/setter。
    2.  **事件監聽**：在 [Canvas.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/Canvas.java) 的建構子中註冊 `mouseMoved` 事件。
    3.  **動態更新**：當滑鼠移動時，如果當前是 `SelectMode`，畫布會呼叫 `findObjectAt(e.getPoint())` 找到滑鼠底下的物件，將該物件的 `hovered` 設為 `true`，其他物件設為 `false`，並觸發 `repaint()`。若非選取模式，則將所有物件的 `hovered` 清空。
    4.  **繪製邏輯**：在 [RectObject.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/RectObject.java) 與 [OvalObject.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/OvalObject.java) 的 `draw()` 方法中，將繪製 Ports 的條件設為 `if (selected || hovered)`。如果是群組 [CompositeObject.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/CompositeObject.java)，則在 `selected || hovered` 時繪製代表群組的外框。

---

### Q6：在 UML 關係線中，你如何做到 Composition 菱形是黑色的，而 Generalization 三角形是白色的，且不透出底下的線條？
*   **回答**：
    *   這是透過 **自訂繪圖順序** 與 **填滿多邊形 (Polygon Filling)** 實現的：
        *   **Composition (實心菱形)**：在 [CompositionLine.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/CompositionLine.java) 中，計算出菱形的四個頂點後，我們先把畫筆設為黑色（`g2.setColor(Color.BLACK)`），呼叫 `g2.fillPolygon(...)` 將內部填滿，最後再用黑色畫出外框。
        *   **Generalization (空心不透明三角形)**：在 [GeneralizationLine.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/GeneralizationLine.java) 中，我們在畫三角形外框之前，先將畫筆設為白色（`g2.setColor(Color.WHITE)`），呼叫 `g2.fillPolygon(...)` 將三角形內部填為白色，這樣就能蓋住底下的連接線，最後再將顏色設回黑色（`g2.setColor(Color.BLACK)`）畫出三角形邊界。

---

### Q7：拖曳建立 Rect/Oval 物件後，工具列按鈕與模式會恢復到原選定狀態，這是怎麼做到的？
*   **回答**：
    *   在 [MainScreen.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/MainScreen.java) 中，我們替 `rectButton` 與 `ovalButton` 加入了 `MouseListener`。
    *   在 **`mousePressed`**（使用者按下按鈕準備拖曳）時，我們先將當前的畫布模式與亮起的按鈕備份起來：
        ```java
        preDragMode = canvas.getCurrentMode();
        preDragButton = currentActiveButton;
        ```
    *   在 **`mouseReleased`**（使用者釋放滑鼠，拖曳建立完成）時，我們進行復原：
        ```java
        if (preDragMode != null) {
            canvas.setCurrentMode(preDragMode);
        }
        if (preDragButton != null) {
            highlightToolButton(preDragButton);
        }
        ```
    *   這樣做就避免了寫死（Hardcode）每次拖曳完都返回 `SelectMode`，能保留使用者原本的工作模式。

---

### Q8：物件在縮放 (Resize) 時，寬高是如何被限制不得小於 20 像素的？
*   **回答**：
    *   在基本圖形（Rect/Oval）中實作了 `resize(PortPosition portPosition, int dx, int dy)` 方法。
    *   該方法會先根據滑鼠移動量 `dx` 與 `dy` 計算出縮放後的 `left`, `right`, `top`, `bottom` 邊界座標。
    *   接著加入防呆判斷：
        ```java
        if (Math.abs(right - left) < MIN_SIZE) { // MIN_SIZE 為 20
            int center = (left + right) / 2;
            left = center - MIN_SIZE / 2;
            right = center + MIN_SIZE / 2;
        }
        if (Math.abs(bottom - top) < MIN_SIZE) {
            int center = (top + bottom) / 2;
            top = center - MIN_SIZE / 2;
            bottom = center + MIN_SIZE / 2;
        }
        ```
    *   如果縮放寬高小於 20 像素，會以當前中心點強制展開為 20 像素，並依此更新圖形的 `x`, `y`, `width`, `height`。

---

## 💡 第三部分：架構與類別關係 (Architecture & Class Relationships) - 佔 15% 分數重點

### Q9：連線（LinkObject）與基本物件（GraphObject）之間是如何產生關係的？Port 扮演了什麼角色？
*   **回答**：
    *   每一條連線（`LinkObject` 的子類別）並不直接依附在 `GraphObject` 上，而是依附在 **`Port` (連接點)** 上。
    *   [LinkObject.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/LinkObject.java) 內部持有 `protected Port startPort` 與 `protected Port endPort` 兩個屬性。
    *   當 `GraphObject` 被移動時，由於 `Port` 獲取坐標是透過 `owner.getPortPoint(position)` 動態向它的擁有者圖形查詢的，因此 `Port` 的座標會自動更新。
    *   連線在繪製時，直接呼叫 `startPort.getPosition()` 與 `endPort.getPosition()` 來取得最新的端點座標，如此一來，關係線就會自動跟著圖形移動而移動。

---

### Q10：請說明 PortPosition 這個 Enum 在專案中的作用？
*   **回答**：
    *   [PortPosition.java](file:///c:/Users/vicky/OneDrive/桌面/NCU2/物件導向/OO/PortPosition.java) 是一個列舉（Enum），定義了 8 個方向的位置：`TOP`, `BOTTOM`, `LEFT`, `RIGHT`, `TOP_LEFT`, `TOP_RIGHT`, `BOTTOM_LEFT`, `BOTTOM_RIGHT`。
    *   它的作用主要有二：
        1.  **Port 定位**：在基本物件的 `getPortPoint(PortPosition position)` 中，依據傳入的列舉值計算該 port 相對於圖形 `(x, y, width, height)` 的絕對坐標（例如 `TOP` 就是 `(x + width/2, y)`）。
        2.  **縮放方向判斷**：在 resize 時，系統需知道使用者拖曳的是哪一個連接點，從而決定要改變的是左邊界、右邊界還是角落（例如拖曳 `TOP_LEFT` 需要同時改變寬、高、X、Y，而拖曳 `RIGHT` 只需要改變寬度）。
