## Stock Watch

### Application Flow Diagrams

![Alt text](pic/Screenshot_1.png?raw=true "main")

<a href="url"><img src="pic/main.png" align="left" height="750" width="400" ></a>  

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
 
### Application Behavior Diagrams:

#### 1) Main Activity

* Each stock entry contains the Stock Symbol (i.e., AAPL), the company name (i.e., Apple Inc.), the Last Trade Price (135.72), the price change direction (▲for positivePrice Change Amount, ▼for negativePrice Change Amount), the Price Change Amount (0.38), and the Price Change Percentage (0.28%) in parentheses.

      If the stock’s Price Change Amount is a positive value, then entire entry should use a green font. 
      If the Price Change Amount is a negative value, then entire entry should use a red font.

* Long-Click on a Stock entry to delete (with delete confirmation)

* Clicking on a Stock entry opens a web browser to the Marketwatch website site for the selected stock

<a href="url"><img src="pic/EditActivity.png" align="left" height="750" width="400" ></a>  

__Note__: Icons for the all menu and edit items are from Google’s Material Design icon set (https://material.io/icons/)

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
<br>
<br>
<br>
<br>
<br>
<br>


