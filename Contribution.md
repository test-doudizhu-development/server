Remarks
For each week, everyone should write down your contributions with following information:
1. name
2. link to the Github issues you worked on
3. optional: a short description of your work

# Week1(Mar 27- April 2)

## Jing Cao
- Map images to different cards#23

## Xiong Li
- Check the user's cards amount #17
- Implement functions to check whether a certain combination is possible #35
- Implement functions to record the cards last played #34

## Zhi Wang
- Define the card number#20
- Implement the card type (about the game rule)#22
- Implement functions to play cards(Controller Part). #34

## Weimin Yang
- short description of my work:define user entity and combinatin typies
    - define 'user' entity: id,name,username, password,token,status  #25
    - define combination types  #18


# Week2(April 3 - April 9)

## Jing Cao
- Implement server-side function to create user#42
- Implement server-side validation to ensure that the user login data is valid and meets all necessary requirements, including password matching and checking that the user exists in the database.#41
- Implement logout function to let users logout if they want #44
- Implement the function to change password#45

## Xiong Li

- Define the card priority(game rule) #19
- Assign cards to different types of players # 26

## Zhi Wang
- Implement functions for the current player to pass #36
- Implement functions to check whether a certain combination is possible（Completed the check rule of single card）#35

## Weimin Yang
- define circularlinkedlist to make sure this is a turn-based game #48
- user can update their profit #49

# Week3(April 10 - April 16)
## Jing Cao
- Implement interceptor to config route to check user login status #50
- Implement functions to set the game status as over when the game is over #30
- Implement the function that compare the cards value, type and combination #33
- Define the card combination type #58
- Implement the function to determine the card combination of Airplane(double three) #59
- Implement the function to define the maximum of "Airplane"(Double Three) #61
- Implement the function to determine the card combination of Consecutive Double #62
- Implement the function to define the maximum of "Consecutive Double" #63


## Weimin Yang
- Landlord campaign, including notifying players to campaign for landlord and confirming landlord #26
- Deal 17 cards to each player and change the game state to the state of campaigning landlord #27
- determine the card combition type whether it is 'bomb' #52
- determine the card combination type whether is 'four and three' and whether it is the biggest value #53
- determine whether the card combination is 'Chain'#55
- exception #57

## Zhi Wang
- Implement function to quit the game room. #31
- Implement functions to continue the game after game ending #32
- Implement functions to play cards(Fix Code). #34
- Implement functions to sync the game(Fix code:sync the game to the players in the same game room). #35
- Judge whether the Poker Combination is a Three-And-One poker combination, and returns the value of any of the three cards in front of the Three-And-One. #74
- Judge whether the Poker Combination is a Three-And-None poker combination #75

## Xiong Li
- define the card priority, resolve #19
- define the card number, resolve #20
- implement card suit, resolve #22
- Create websocket and game initiation, resolve #51
- get poker combination type, resolve #60
- Get the current player's information, resolve #65
- Create the game room, resolve #66
- Implment functions to add user for a game room, resolve #67
- Get the current player's information, resolve #65
- Create the game room, resolve #66
- Implement functions to add user for a game room, resolve #67
- Implement functions to add user for a game room, fix #67
- Create a WebSocket controller class for synchronization, resolve #68
- Implement functions to check whether the current user is already in the room, resolve #69
- Implement functions to monitor messages, resolve #70
- Create a WebSocket controller class for room information synchronization, resolve #71
- Implement functions to check whether the card combination is "Double Cards", resolve #72
- Implement functions to check whether the card combination is "Double Jokers", resolve #73

# Week4(April 17 - April 24)
## Jing Cao
- Add images for cards, background and figures #21
- Create dashboard components #24
- Create componnets to play background music #33
- Create login page components #29
- Create register page components #25
- Define the types of values and properties #23
- Implement function to get card and sort the card #35
- Add testing to create game#108
- Add testing for update detail#122
- Add testing for register#121
- Add testing initiation#115
- Add testing to pass#110
- debug
- write the M3 report
## Weimin Yang
- prepare buttons #12
- The profile page and user can edit profile #17
- prepare background for the app #19
- prepare api tools for app #22
- reset passwords #30
- Exception #28
- Theme #32
- add testing to create game #112
- add testing about contend #113
- add testing on continueGame #114
- add testing on initial user #117
- add testing on get user detail #119
- add testing on offline #120
- debug
- write the M3 report
## Zhi Wang
- Client:
- Set schema rules for necesary fileds like username and password(client) #2
- Create component to display cards when players play the game(client) #27
- Create homepage component(client) #34
- Create a component to verify the user's account(client) #36

- Test:
- Test Add User to the game room #107
- Test play cards #109
- Test user login #118
- Test update password #123
- Debug
- Write the M3 report

## XiongLi
- Create user information fields information like username, password and etc, resolve #31
- Create overview page component, resolve #38
- Create room component and integrate it with WebSocket, resolve #39
- Create verify confirmation component, resolve #40
- Implement some basic configuration, resolve #41
- Write the M3 report


# Week5(April 25 - May 2)
## Jing Cao
- Add testing for updateUserDetail, updatePassword, offline, createuser, getuserbytoken, login, resolve #137
- Re-upload image and icon, fix #26
- Change confirmation message, fix #40
- Update dashboard, fix #6
- Debug
- Presentation Preparation

## Zhi Wang
- test play cards function(fix code in the gameContextUnderTest)
- test quit game function(fix code in the gameContextUnderTest)
- Modify the configuration file to complete the Google Cloud deployment
- Modify the configuration file to complete the Sonar Cloud deployment
- test play cards function(fix code)
- test play cards function(fix code)
- Debug
- Presentation Preparation

## Xiong Li
- Improve the UI
- Debug
- Prepare report

## Weimin Yang
- add integration test 
- add testing on roomsync
- Debug
- Presentation Preparation

# Week6(May 3 - May 9)
## Jing Cao
- Implement testing to initiation to check user login status #139
- Implement testing for the function to login by name #140
- Implement testing for the function to login by token #141
- Implement testing for the function to login by username #142

## Zhi Wang
- Add test the use of poker Combination(double cards, three cards, three card and one) # 131
- Add test the use of poker Combination(consecutive double cards) # 132

## XiongLi
- Design an intuitive and user-friendly interface that makes it easy for players to navigate the game and understand the controls. #128
- Make the result of the game to be seen obviously. #129

## Weimin Yang
- add testing on inital game(include user, cards) # 130
- add testing on play game(including running for the landlord, deal cards) # 136

# Week6(May 10 - May 16)
## Jing Cao
- Add codes to improve testing coverage of UserReqVo to change the password #143
- Add testing for exceptions when the password is different #144

## Zhi Wang
- Add test the use of poker Combination(four cards, four cards and two) # 134
- Add test the use of poker Combination(consecutive double cards,consecutive three cards) # 133


