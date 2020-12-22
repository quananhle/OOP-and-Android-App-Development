## News Gateway

##### Published Date: 12/03/2020

### Application Flow Diagrams

![Alt text](photo/application_architecture.png?raw=true "main-page")

<a href="pic/main_page.png"><img src="pic/main_page.png" align="left" height="750" width="400" ></a>  

* This app will acquire and display an interactive list of political officials that represent the current location (or a specified location) at each level of government.

* Android location services will be used to determine the user’s location.

* The [Google Civic Information API](https://developers.google.com/civic-information/) will be used to acquire the government official data (via ```REST``` service and
```JSON``` results). .

* Clicking on an official’s list entry opens a detailed view of that individual government representative.

* An ```About``` activity will show application information (Author, Copyright data & Version)

* Clicking on the photo of an official will display a ```Photo Activity```, showing a larger version of the photo.

* Permissions for `ACCESS_FINE_LOCATION` and `INTERNET`

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

<a href="pic/application_behavior_main.png"><img src="pic/application_behavior_main.png" align="center" height="850" width="800" ></a>  

__Note__: Icons for the all menu and edit items are from Google’s Material Design icon set (https://material.io/icons/)

> 1. Manually setting the location - Enter City, State (E.g.: Chicago, IL; Las Vegas, NV) or Zip Code (E.g.: 94027, 33480, 60043)

<p float="center">
 <img src="pic/dialog.png" height="600" width="300" />
 <img src="pic/atherton_dialog.png" height="600" width="300" />
 <img src="pic/Atherton.png" height="600" width="330" />
</p>

> 2. Opening the info page:

<a href="pic/info.png">
<div align="center"><img src="pic/info.png" height="750" width="400" ></div>
</a>

<br>

>> Clicking on [Google Civic Information API](https://developers.google.com/civic-information/) shall navigate the user to the API website. Clicking on developer's name shall direct the user to the Github page [Quananhle](https://github.com/Quananhle) or the all logo shall direct the user to the Github page [KnowYourGovernment](https://github.com/Quananhle/OOP-and-Android-App-Development/tree/master/Android-App-Developer/App/KnowYourGovernment) where the source code of the application is published.

<br>

> 3. Opening the view of an individual official for details:

![Alt text](pic/official_activity_1.png?raw=true "official_activity_page1")

![Alt text](pic/official_activity_2.png?raw=true "official_activity_page2")

<p float="center">
 <img src="pic/official_details.png" height="750" width="400" />
 <img src="pic/official_details_1.png" height="750" width="400" />
</p>

>> Landscape view:

<a href="pic/official_details_2.png">
<div align="center"><img src="pic/official_details_2.png" height="500" width="1100" ></div>
</a>

<br>

