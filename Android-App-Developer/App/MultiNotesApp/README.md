## Multi-Note Pad

### Application Flow Diagrams

![Alt text](pic/Application-Flow-Diagrams_1.png?raw=true "1")
 
![Alt text](pic/Application-Flow-Diagrams_2.png?raw=true "1")

### How-to-use

##### 1) Main Activity

<a href="url"><img src="pic/MainActivity.png" align="left" height="750" width="400" ></a>  

* Notes are displayed in a list, in time order (latest-update-first, oldest-update-last).

* The ```Main Activity``` allows the user to create a new note via an Addoptions-menu item . Pressing this button will open the ```Edit Activity```(described next) with empty ```Title``` and ```Note Text``` areas.

* The main activity will allow the user to edit an existing note by tapping on an existing note in the displayed list of notes. Doing so will open the ```Edit Activity```, displaying the note’s ```Title``` and ```Note Text``` – both available for editing.

* The main activity will also have an ```Info``` options-menu item that will open the ```About Activity```(described later) when pressed. The ```About Activity``` indicates the application’s name, the date & author, and the version number.

* Notes can be deleted from the ```Main Activity``` by long-pressing on a note entry. Upon doing so, a confirmation dialog will be opened where the user can confirm (or cancel) the delete operation.

* The current number of notes is displayed in the title-bar.

