import serial
import json
import time
import requests

if __name__ == '__main__':
    
    ser = serial.Serial(port='COM4', baudrate=9600, timeout=0)
    print("starting script : \n")
    while True:
    # Get workout data form Arduino
        while ser.inWaiting() < 0:
            time.sleep(0.1)
        print("done waiting")
        
        workout_data = ser.readline()
        print("data : "+ str(workout_data))
        print(len(workout_data))
        time.sleep(5)

        if len(workout_data) > 6 :
            workout_data = workout_data.decode()[:-2].split(",")
            # Tembak ke server
            if workout_data[0] == "pushup" :
                weight = workout_data[2]
                total = workout_data[1]
                r = requests.get("http://localhost/getCaloryPushup/?weight="+weight+"&total="+total)
                if r.status_code == 200:
                    calories = r.json()['calories_total']
                    print('Calories: ', calories)
                    # # Tembak ke Arduino
                    ser.write(str.encode(calories))
                    testread = ser.readline()
                    print("dari arduino : "+ str(testread))
                    break
                else:
                    print('Error nge-GET tapi gatau napa')
            elif workout_data[0] == "situp" :
                weight = workout_data[2]
                total = workout_data[1]
                r = requests.get("http://localhost/getCalorySitup/?weight="+weight+"&total="+total)
                if r.status_code == 200:
                    calories = r.json()['calories_total']
                    print('Calories: ', calories)
                    # # Tembak ke Arduino
                    ser.write(str.encode(calories))
                    testread = ser.readline()
                    print("dari arduino : "+ str(testread))
                    break
                else:
                    print('Error nge-GET tapi gatau napa')
            elif workout_data[0] == "jogging" :
                weight = workout_data[2]
                total = workout_data[1]
                r = requests.get("http://localhost/getCaloryJogging/?weight="+weight+"&total="+total)
                if r.status_code == 200:
                    calories = r.json()['calories_total']
                    print('Calories: ', calories)
                    # # Tembak ke Arduino
                    ser.write(str.encode(calories))
                    testread = ser.readline()
                    print("dari arduino : "+ str(testread))
                    break
                else:
                    print('Error nge-GET tapi gatau napa')
        else:
            print("Salah kirim data gak tau napa")