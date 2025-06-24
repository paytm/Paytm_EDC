
import os
import json

from paytm_payments import payments
p = payments.Payments()

def get_configs():
    if not os.path.isfile("config.json"):
        raise Exception("config.json missing at root path of project")
    
    with open("config.json") as user_file:
        file_contents = user_file.read()
    file_contents = json.loads(file_contents)
    return file_contents

def initiate():
    display_flag = True
    while display_flag:
        print("Type command or Press q to quit prompt")
        value = input()
        value = value.strip()
        configs = get_configs()
        if value and value != "q":
            resp = getattr(p, value)(**configs.get(value))
            print("********** OUTPUT *************")
            print(resp)
        else:
            display_flag = False

initiate()

