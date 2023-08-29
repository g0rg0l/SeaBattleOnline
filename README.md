# SeaBattle

## Main description
This project is the first Java program - ***sea battle*** with original rules.

## Rules of the game
For implementation, the original rules of this board game were taken. More specifically, they can be read at [wikipedia](https://en.wikipedia.org/wiki/Battleship_(game))

In short, the players take turns naming the positions where the strike will be made on a 10x10 field, with 5 ships placed. Ships cannot be spaced close to each other. And also the ship is considered destroyed if all its positions were attacked, in this case, the ship on the field is surrounded by "attacked" cells. The winner is the one who first destroys all the opponent's ships.

## Launch from one widget
If players prefer to play from a single device (A little pointless, right? :З ), then the game is started by working with an `Engine` object that runs the game in a single console window. Players take turns writing moves.
In this case, it will be enough to run `Main.main()`.

This build is `MonoPlaySeaBattle.jar` in ***Releases***.

## Launch from many widget (many devices)
If players prefer to play each from their own device, then ***they need to connect to the same network.***

- The server must be started first. Entry point - `Server.main()`. This build is `ServerSeaBattle.jar` in ***Releases***.
- Next, players who want to connect must run the client side on their device. The entry point is `Client.main()`. This build is `ClientSeaBattle.jar` in ***Releases***.

***(NOTE)*** When a client connects, it must enter the ***internal ip address*** of the device hosted by the server. To find out, on the server device, you can use the `ipconfig` command (in Windows) - the desired address will be in the line `IPv4 ...`

## Running .jar files
All `.jar` archives are started with the command `java -jar [archive_name].jar` in the console/terminal.

# Preview
On the left is the player's own board, and on the right is the opponent's board.

The blue cells indicate the unattacked positions of the player's ships, and the red cells indicate the attacked ones.

On the enemy board, misses are marked in green, and the places where they hit the positions of enemy ships are marked in red.

![preview_2](https://github.com/g0rg0l/SeaBattle_JavaHelloWorld/blob/master/preview/preview_2.png)

![preview_3](https://github.com/g0rg0l/SeaBattle_JavaHelloWorld/blob/master/preview/preview_3.png)

@g0rg0l
