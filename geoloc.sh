#!/bin/bash
# init
function pause(){
  read -p "$*"
}

pause 'Press [Enter] key to continue...'
echo "geo fix 4,827847 45,77797" | nc localhost 5554
pause 'Press [Enter] key to continue...'
echo "geo fix 4,83215 45,773778" | nc localhost 5554
pause 'Press [Enter] key to continue...'
echo "geo fix 4,833746 45,776147" | nc localhost 5554
pause 'Press [Enter] key to continue...'
echo "geo fix 4,835787 45,780095" | nc localhost 5554