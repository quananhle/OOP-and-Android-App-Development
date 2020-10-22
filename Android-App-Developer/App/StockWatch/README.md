## Stock Watch

### Application Flow Diagrams

![Alt text](pic/Screenshot_1.png?raw=true "main")
 
### How-to-use

#### 1) Main Activity

<a href="url"><img src="pic/main.png" align="left" height="750" width="400" ></a>  

* Notes are displayed in a list, in time order (latest-update-first, oldest-update-last).

* The ```Main Activity``` allows the user to create a new note via an Addoptions-menu item . Pressing this button will open the ```Edit Activity```(described next) with empty ```Title``` and ```Note Text``` areas.

* The main activity will allow the user to edit an existing note by tapping on an existing note in the displayed list of notes. Doing so will open the ```Edit Activity```, displaying the note’s ```Title``` and ```Note Text``` – both available for editing.

* The main activity will also have an ```Info``` options-menu item that will open the ```About Activity```(described later) when pressed. The ```About Activity``` indicates the application’s name, the date & author, and the version number.

* Notes can be deleted from the ```Main Activity``` by long-pressing on a note entry. Upon doing so, a confirmation dialog will be opened where the user can confirm (or cancel) the delete operation.

* The current number of notes is displayed in the title-bar.

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

#### 2) Edit Activity

<a href="url"><img src="pic/EditActivity.png" align="left" height="750" width="400" ></a>  

* The ```Edit Activity``` allows the note title & text to be saved by either:

     1. Pressing the ```Save``` options-menu item. This will return the new note to the ```Main Activity```, and then exit the ```Edit Activity```. ```Main Activity``` will then add the note to the ```Main Activity```’s list of notes. Note that if no changes have been made to the current note, the ```Edit Activity``` simply exits.

     2. Pressing the Back arrow to exit the activity. This will first display a confirmation dialog where the user can opt to save the note (if changes have been made) before exiting the activity. If saved, the new noteis returned to the ```Main Activity```, and then exit the ```Edit Activity```. ```Main Activity``` will then add the note to the ```Main Activity```’s list of notes. Note that if no changes have been made to the current note, the ```Edit Activity``` simply exits.
     
* A note without a title is not allowed to be saved(even if there is note text). If such an attempt is made simply exit the activity without saving and show a Toast message indicating that the un-titled activity was not saved.

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

#### 3) About Activity

<a href="url"><img src="pic/AboutActivity.png" align="left" height="750" width="400" ></a> 

* There is no functionality present on this activity. The only action a user can take is to press the Back arrow to exit the activity.
