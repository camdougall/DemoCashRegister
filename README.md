# DemoCashRegister
## Assumptions made
* No micro transactions of amounts lower than a penny. For example, I did not account for things like micro transactions or gasoline purchase
* No multithreading
* No persistence between server restarts
* Not concurrent
* Assumed that payment is in more efficient configuration of denominations. No little kids paying for a $20 toy with a roll of quarters
* Assumed use of $2 and $.50 pieces
* Assumed register started out with 0 of each denomination and had to be built up from transaction amounts
## Things I didn't Include for simplicity
* Logging: the log4j nightmare is on my mind, but I wanted to keep this simple
* Docker File
* Multiple Environments : for production quality code I would assume multiple environments and properties files (or properties served from server/ environment)
* More robust error handling : production quality code should include much more robust exception handling
* Angular/React 
* Selenium Testing
* Authentication and Authorization : production quality code would have to include who is able to make change out of the register and who is not
