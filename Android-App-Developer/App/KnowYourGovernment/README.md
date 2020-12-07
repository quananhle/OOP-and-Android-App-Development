## Know Your Government

##### Published Date: 11/11/2020

### Application Flow Diagrams

![Alt text](pic/main-page.png?raw=true "main-page")

<a href="pic/main_page.png"><img src="pic/main_page.png" align="left" height="750" width="400" ></a>  

* This app allows the user to display a sorted list of selected stocks. List entries include the stock ```symbol``` (and ```company name```), the ```current price```, the ```daily price change amount``` and ```price percent change```.

* There is no need to use a different layout for landscape orientation in this application as the same layout should work in any orientation.

* Selected stock symbols and the related names should be stored in the device’s ```SQLite Database```.

* A ```Stock``` class should be created to represent each individual stock in the application. Required data includes: ```Stock Symbol``` (String), ```Company Name``` (String), ```Price``` (double), ```Price Change``` (double), and ```Change Percentage``` (double). 

* Clicking on a stock opens a browser displaying the Marketwatch webpage for that stock

* ```Swipe-Refresh``` (pull-down) refreshes stock data.

<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
 
### Application Behavior Diagrams:

#### 1) Main Activity

* Each stock entry contains the Stock Symbol (i.e., AAPL), the company name (i.e., Apple Inc.), the Last Trade Price (135.72), the price change direction (▲for positivePrice Change Amount, ▼for negativePrice Change Amount), the Price Change Amount (0.38), and the Price Change Percentage (0.28%) in parentheses.

      If the stock’s Price Change Amount is a positive value, then entire entry should use a green font. 
      If the Price Change Amount is a negative value, then entire entry should use a red font.

* Long-Click on a Stock entry to delete (with delete confirmation)

* Clicking on a Stock entry opens a web browser to the Marketwatch website site for the selected stock

<a href="url"><img src="pic/Screenshot_marketwatch.png" align="left" height="750" width="1100" ></a>  

__Note__: Icons for the all menu and edit items are from Google’s Material Design icon set (https://material.io/icons/)

1. Adding a stock – when only one stock matches the search symbol/name search string (__NOTE__: The Stock Selection dialog only allow capital letters):
<a href="pic/addStockDialog.png">
<div align="center"><img src="pic/addStockDialog.png" height="750" width="400" ></div>
</a>


2. Adding a stock – when multiple stocks matched the search string:
<a href="pic/stockSelectionDialog.png">
<div align="center"><img src="pic/stockSelectionDialog.png" height="750" width="400" ></div>
</a>


3. Adding a stock with no Network Connection – test using “Airplane Mode”(No buttons on the error dialog):
<a href="pic/Screenshot_airplane_1.png">
<div align="center"><img src="pic/Screenshot_airplane_1.png" height="750" width="1100" ></div>
</a>



