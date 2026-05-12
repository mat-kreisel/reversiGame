# Reversi (Java, MVC)

This project is an implementation of the classic board game Reversi (Othello) in Java.  
The main goal was to design a clean separation between game logic and user interface using the MVC architecture.

## Features

- Classic Reversi gameplay (2 players)
- Full implementation of game rules including valid moves and piece flipping
- Graphical user interface built with Java Swing
- Clear separation of Model, View, and Controller
- Structured and maintainable codebase
  
## AI Opponent

The game includes a simple AI opponent based on a decision tree approach.

- The AI evaluates all possible moves by analyzing the current board state  
- Each possible move is assigned a score based on a heuristic evaluation  
- The move with the highest score is selected  

Additionally, the lookahead depth can be configured, allowing the AI to simulate multiple future moves and make more strategic decisions.

This was implemented to explore basic concepts of game AI and decision-making algorithms.

## Architecture

The project follows the MVC (Model-View-Controller) pattern:

- **Model**: Contains the game logic and state  
- **View**: Implements the graphical user interface using Java Swing  
- **Controller**: Handles user input and controls the game flow  

The goal was to keep these components as independent as possible.

## Technologies

- Java  
- Java Swing (GUI)  
- MVC architecture  

## Learning Objectives

- Applying object-oriented programming principles  
- Implementing a common design pattern (MVC)  
- Separating business logic from UI  
- Structuring a medium-sized project  

## Note

This project was developed as part of a university course.  
The focus was on clean architecture and implementation rather than advanced features or multiplayer support.
