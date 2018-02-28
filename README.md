# Project Title

This is a school project in Java.
The aim  of the project was to learn Java programming language features by programming a network card Game. It's a french game named "La belotte coinchée".

For more informations about the game, see the official [Belotte Coinchée Website](http://www.beloteweb.eu/regles-de-la-belote-coinchee/).

## Getting Started

To clone the repository:
```
git clone git@git@epitech.eu:/girard_z/Java_jcoinche_2017
cd Java_jcoinche_2017
```



### Prerequisites

You should install [Maven](https://maven.apache.org/plugins/maven-compiler-plugin/index.html) and have at least the 8th version of [Jdk](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).


### Installing

First of all, install the dependencies and the environment:

```
mvn install
```

Compile sources:

```
mvn compile
```

Create packages:
```
mvn package
```

### Running

```
java -jar target/jcoinche-client.jar [ip address] [port]
java -jar target/jcoinche-server.jar [port]
```

## Documentation
open ``` target/site/apidocs/index.html ```
or
open ``` doxy/html/index.html```

## Description

The is a multiroom network game of coinche.

### Rooms
Each player is able to create/join a room. When four players are in a room, the party begins.
When a player leaves a room / disconnect, the party is over and the room is waiting for a fourth player.

### The Game

When 4 players are ready in a room, the first player to be played can call.
- If nobody calls, cards are redistributed
- if Somone Calls, the three next players have to skip there call to let the game begins.


## The Commands
you can type ``` help ```  to see the help.

```
help			->		Helper
createroom		->		Ask to create a room and join it if authorized
leave			->		Ask to leave a room and leaves it if authorized 
disconnect		->		Disconnect client
roomlist		->		Show the list of rooms
join			->		Try to join a room
ready			->		Say to the room that you are ready to play
showmycards		->		Display cards in your hands
call			->		Make a call during the calling phase
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management


## Authors

* **Yohann N'Goala** - *Software developer FrontEnd* - [YoyoLeOuf](https://github.com/YohannNgoala)
* **Benjamin Girard** - *Software developer BackEnd* - [Têtard](https://github.com/BenjaminGirard)


## License

This project is licensed under the EPITECH License.