# Lota Chilena (bingo)

###A simple bingo game based on the chilean version called lota.

The app is mostly based on using the Text too speech. Basically the app mimics the behaviour of a bingo caller person. The user will be able to play bingo in an organized manner, knowing every chip played and awarding the winner players.

I hope you like it :) Thanks.

**Contributions are welcome**

##TO-DO
1.- Text to speech usage is redundant, it should be more DRY
2.- Photos should be handle properly without repeating code in both activites
3.- Photos should be set in the correct orientation, currenly are set by default in landscape
4.- Create a UI for managing players
5.- Improve the icons for winning (start) and flirt (heart), generally and also when a winner is selected
6.- Improve the performance of players list, is very slow, dont know why...

##Description

**GameListActivity:** Contains a recyclerview with all the created games, Im using a broadcast to trigger the actions on click and on long click. The fab button will lead the user to the game creation.
**CreateGameActivity:** The game name is requiered but photo is optional. Saving the game will take the user to add players for that game.
**AddPlayersActivity:** The user can select to add new players, use some previous stored players or just play a game anonymously
**BingoCallerActivity:** Use 3 fragments for creating the overall game
*CallerFragment:* The user can roll the raffle box and get a number, the below button will repeat the last number played
*NumbersFragment:* Is a list view with all the numbers played
*PlayersFragment:* A list of the players, on click players can be set as winners, and in the top the winners can be removed (in case of error), on long click

###Project Structure
**adapters:** One adapter per model
**models:** The models used for data and the Queries associate to each model
**views:** Activities and fragments, for details see above

##Dependencies
**compile 'com.github.satyan:sugar:1.4'** This is a very good ORM, I really like it, is used for handle the data persistence, please read the docs if needed, http://satyan.github.io/sugar/

**compile 'com.android.support:cardview-v7:23.2.0** The standard CardView dependencie for using cards


