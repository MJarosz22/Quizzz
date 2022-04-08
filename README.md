<!-- This README will need to contain a description of your project, how to run it, how to set up the development environment, and who worked on it.
This information can be added throughout the course, except for the names of the group members.
Add your own name (do not add the names for others!) to the section below. -->

# OOP-Project Group 8

## Description of project

The goal of this project is to create a Quizzz-like game, focussing on energy sustainability. The project will be based on a Spring-based back-end
with a OpenJFX front-end. This project is made over a period of 10 weeks in which we learn to use new frameworks, collaborate and much more.

## Group members

| Profile Picture                                                                           | Name                                 | Email                                           |
|-------------------------------------------------------------------------------------------|--------------------------------------|-------------------------------------------------|
| ![](https://secure.gravatar.com/avatar/7fe070246a92ce953af396a64d04a0d3?s=50&d=identicon) | Rafael Petouris Rodriguez de Paterna | R.PetourisRodriguezdePaterna@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/b34e0f2e1f8e93c260888e6a32e444ad?s=50&d=identicon) | Petra Postelnicu                     | P.Postelnicu@student.tudelft.nl                 |
| ![](https://secure.gravatar.com/avatar/320347b027870d81f40a5396e10692de?s=50&d=identicon) | Vlad Nitu                            | V.P.Nitu@student.tudelft.nl                     |
| ![](https://secure.gravatar.com/avatar/c4eb41585358f0a7519d599090127aee?s=50&d=identicon) | Marcin Jarosz                        | M.W.Jarosz@student.tudelft.nl                   |
| ![](https://secure.gravatar.com/avatar/a61f42b8f8a74b504d383600541bd428?s=50&d=identicon) | Joshua Gort                          | J.Gort@student.tudelft.nl                       |
| ![](https://secure.gravatar.com/avatar/a5b5fb2a538ff52ce5cf1c7ec9ff1a6b?s=50&d=identicon) | Sophie van der Linden                | S.A.vanderLinden@student.tudelft.nl             |

## How to run it
### How to add the activities to the repository
After cloning the repository on your machine, we provide 2 options to follow for adding the activities to the repository: <br />
**NOTE**: We added the unzipper in order to make the repository more user-friendly, therefore we advise you to go for step 1. <br />
1. **Directly adding the .zip file**: <br /> "activities.zip" (that contains activities.json file and the photos related to the activites) should be downloaded from the following link: https://gitlab.ewi.tudelft.nl/cse1105/2021-2022/activity-bank/-/releases.
<br />
After accessing the link, you need to click on the other link pointed below in order to get the required .zip file donwloaded on your machine.
![](https://gitlab.ewi.tudelft.nl/cse1105/2021-2022/team-repositories/oopp-group-08/repository-template/uploads/239e739f506f25dd2bf18c69d17dcf8e/activitiesziplocation.png)
<br />
Then, it should be renamed as "activities.zip" and added in the "resources" folder from the server-side, that is located on the following path: "server/src/main/resources/{here should the activities.zip be added}"
<br />
This can be simply done by drag-and-dropping the "activities.zip" in the correct located "resources" folder, the one described above.
<br />
   If the zipper is added accordingly (named as activities.zip and containing the correct content, the one downloaded by accesing previous links), the server will inform you that it 'Succesfully unzipped activities.zip' and then "Activities added to repo".
    <br /> If this is not the case, the server will let you know that a problem occured (such as: "activities.json was not found"). 
2. **Directly adding the activities.json file and all photos**: <br />
   If you do not want to make use of our unzipper (that's integrated in the code), you can manually add the activities.json and all 78 folders with images) in "resources/activities/{insert_here}", where the "activities" folder will be manually added by you.
<br />

### How to set up the game
After the activities are added to the correct folder, the game will be run using Gradle. You should follow the following steps
1. Make sure you have Gradle installed on your machin (run 'Gradle -version' in terminal. If the command appears as "not found", then you need to install Gradle).
2. Open your chosen InteliJ, press the bottom-left corner (which will provide some options) and then press on "Gradle".
3. Now, you should see Gradle tool window on the right of your IDEA. Click on "Tasks/application (in other words, the first directory and its first subdirectory).
4. Now you should see two executables: 'bootRun' and 'run'. By clicking on 'bootRun' the server should start (only one instance at a time). Then, by clicking on 'run' a client instance will start (more instances can be run at a time).
5. If all the previous steps were followed accordingly, now you should be able to play the game. Enjoy!

## How to contribute to it
As there is always room for improvement, future users can contribute as following
1. If you spot any bugs, please let us know in order to fix the product as soon as possible. Our emails can be found in the "Group members" section.
2. If you have any idea that is relevant in improving our game, we are looking forward to hearing your proposals.

## Copyright / License (opt.)
