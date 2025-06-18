## Python Library for EDC wired Integration

### How to use library
```
from payments import payments
p = payments.Payments()
resp = p.connection_check("/dev/tty.usbmodem14930718862", 115200, 0, 8, 1, 1)
print(resp)
```
### Setup Test Project
uzip test_lib_project tar file
```
tar -xvzf test_lib_project.tar.gz
```
change to project directory
```
cd test_lib_project/
```
create python virtualenv for project
```
python3 -m venv env
```
activate virtualenv
```
source env/bin/activate
```

### Installation

add package tar file path in requirements.txt
```
pip install -r requirements.txt
```
save PaytmPayments.json file at root path of application with following config
```
{
    "StatusCheckOnSaleRequestEnabled": true,
    "ReadWriteOnTerminalTimeout": 6,
    "SaleRequestTimeout": 180,
    "SaleAckStatusCode": ["001", "1100", "1101", "1103"],
    "StatusCheckStart": 10,
    "StatusCheckInterval": 5,
    "TransactionPendingStatusCodes": ["103", "1100", "1101", "1103"],
    "DeviceInstance": "VID:PID=2FB8:2205"
}
```
run test.py
```
python3 test.py
```
Successful run
```
Type command or Press q to quit prompt
```
### How to use Demo
enter command name as mentioned in config.json
```
Type command or Press q to quit prompt
ConnectionCheck
```
output
```
********** OUTPUT *************
{"statusCode":"000","statusMessage":"Command Initiated Successfully"}
Type command or Press q to quit prompt
```

### ConnectionCheck arguments
ConnectionCheck(port_name: str, baud_rate: int, parity: int, databits: int, stopbit: int, debugmode: int)
