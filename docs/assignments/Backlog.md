# MoSCoW Backlog OOP-Project

## Must have

- As a player, I want a Quiz game.
- As a player, I want the quiz to be about energy awareness.
- As a player, I want to join a multiplayer lobby in order to play the game.
- As a player, I want to be able to answer multiple choice questions.
- As a player, I want to have 3 answers per question.
- As a player, I want every question to have an allowed time so the game doesn't take too long.
- As a player, I want to see the correct answer once the allocated time has run out.
- As an admin, I want to establish a server-client connection so that the game can be played multiplayer.
- As a player, I want synchronous questions for all players.
- As an admin, I want past game data to be saved to a database.
- As an admin, I want to add new questions to the database.
- As a player, I want an aesthetically pleasing and accessible design
- As a player, I want units to be according to the metric system.

## Should have

- As a player, I want to have 20 questions per game.
- As a player, I want to have power-ups so I can make hard questions easier to solve.
- As a player, I want the game to continue if someone else leaves.
- As an admin if the game starts, new players are added to the next-available lobby
- As a player, I want the possibility to play in a single-player lobby.
- As a player, I want to have a score that indicates the time-weighted number of correct answers.
- As a player, I want a leaderboard so I can see how I'm doing compared to other players.
- As a player, I want to be able to play with at least 10 players or more.
- As a player, I want to be able to input and show my name into the game without authentication.
- As a player, I want a START button in the waiting room that needs to be pressed by only one player from that specific lobby.
- As a player, I want to get more points if I answer faster.
- As a player, I want to see my highest score from previously played games.
- As a player, I want to be able to use multiple power-ups in the same round.
- As a player, I want the open questions to have a number as an answer.
- As an admin, I want certain power-ups to be disabled for the questions where they can’t be applied.
- As an admin, I want each question to be chosen randomly by type.
- As an admin, I want a second player that inputs a name that was previously selected in that game to receive an error message and to be asked to
  choose another name.

## Could have

- As a player, I want at least 3 question types.
- As a player, I want to be able to start the game once a minimum number of players has been reached.
- As a player, after every question I want to be able to see who answered the question right or wrong
- As a player, I want a disconnected player to be removed from the leaderboard.
- As an admin, I want to select questions from the question bank at the beginning instead of changing it after selection.
- As a player, I want to see how many players are in the waiting room
- As a player, I want a countdown of 5 seconds between the button press and start so that extra players can join.
- As a player, I want to have nice graphics embedded in the questions, such as an image attached to the text.
- As a player, I want to give reactions to quiz questions.
- As a player, I want the reactions to be emojis
- As a player, I want to have more power-ups than the 3 default ones
- As a player, I want the time of a given question to be shortened by a percentage when that specific power-up is used.
- As a player, I want one power-up to be one where you remove 1 incorrect answer.
- As a player, I want one power-up to double my points if I’m sure of my response.
- As a player, I want one power-up which decreases the time for everyone else.
- As an admin, I want a message to be displayed every time a player disconnects.

## Won’t have

- As a player, I do not want a chat feature.
- As an admin, on collisions due to two players having the same name, the second player does not get placed inside the lobby until the name is
  inserted.
- As a player, I do not want each power-up to be used more than once per game.
- As a player, I don’t want to have seperate rooms for private games.
- As a player, I don’t want to have to authenticate to join a game.
- As a player, I don’t want other players to be able to join once a game has started.
- As a player I don’t want the jokers to work for inappropriate questions.
- As an admin, I don’t want the application to use WebSockets.
