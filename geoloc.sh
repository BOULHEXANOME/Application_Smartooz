#!/bin/bash
# init
function pause(){
  read -p "$*"
}

pause 'Press [Enter] key to continue...'
echo "geo fix 4,843479 45,757899" | nc localhost 5554
pause 'Press [Enter] key to continue...'
echo "geo fix 4,842519 45,757869" | nc localhost 5554
pause 'Press [Enter] key to continue...'
echo "geo fix 4,845434 45,766423" | nc localhost 5554
pause 'Press [Enter] key to continue...'
echo "geo fix 4,85983 45,752136" | nc localhost 5554
pause 'Press [Enter] key to continue...'
echo "geo fix 4,872514 45,762229" | nc localhost 5554

