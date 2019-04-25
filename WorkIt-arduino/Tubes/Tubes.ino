/* www.learningbuz.com */
/*Impport following Libraries*/
#define echoPin 7
#define trigPin 8
#include "Timer.h" //include timer library
#include <Wire.h> 
#include <LiquidCrystal_I2C.h>
//I2C pins declaration
LiquidCrystal_I2C lcd(0x27, 2, 1, 0, 4, 5, 6, 7, 3, POSITIVE); 

//Button + PWM
int inPin = 2;
int outPin = 3;

int state = HIGH;
int reading;
int previous = LOW;
int tempState;

unsigned long time = 0;
unsigned long debounce = 200UL;

//Declaring some global variables
int gyro_x, gyro_y, gyro_z;
long gyro_x_cal, gyro_y_cal, gyro_z_cal;
boolean set_gyro_angles;

long accelX, accelY, accelZ;
float gForceX, gForceY, gForceZ;
long gyroX, gyroY, gyroZ;
float rotX, rotY, rotZ;

unsigned long previousMillis = 0;
const long interval = 1000;
unsigned long currentMillis;
int flag = 0;

long acc_x, acc_y, acc_z, acc_total_vector;
float angle_roll_acc, angle_pitch_acc;

float angle_pitch, angle_roll;
int angle_pitch_buffer, angle_roll_buffer;
float angle_pitch_output, angle_roll_output;

long loop_timer;
int temp;
bool isStartSitUp = true;
bool isStartPushUp = true;
int countWorkOut = 0;

long duration;
long distance;

Timer t; // craete a timer object
int first_digit = 0;
int second_digit = 0;
int third_digit = 0;
int fourth_digit = 0;
int timer_event = 0;
int CA_1 = 12;
int CA_2 = 11;
int CA_3 = 10;
int CA_4 = 9;
int clk = 6;
int latch = 5;
int data = 4;
int count = 0;
int digits[4] ;
int CAS[4] = {12, 11, 10, 9};
byte numbers[10] {B11111100, B01100000, B11011010, B11110010, B01100110, B10110110, B10111110, B11100000, B11111110, B11110110};


//Data from Back-end
String workout_type;
String total_workout;
String calories;
String calories_data;
String weight;
int total;
String workout_data;

bool light = false;

void setup() 
{
  pinMode(inPin,  INPUT);
  pinMode(outPin, OUTPUT);
  Serial.begin(9600);

  setup_for_lcd();
  setup_for_led();
  setup_for_situp();
  setup_for_pushup();
  setup_for_jogging();
}

void loop() 
{
  read_bt();
  if (workout_type != NULL && total_workout != NULL && weight != NULL && total != NULL && calories_data != NULL) {
    serial_com();
  }
  if(calories != NULL){
    Serial.println(calories);
  }
  delay(5000);

  if (workout_type != NULL && total_workout != NULL && weight != NULL && total != NULL && calories_data != NULL) {
    introduction();
    if (workout_type == "situp") {
      situp();
    } else if (workout_type == "pushup") {
      pushup();
    } else {
      jogging();
    }
    complete();
  }
}

void setup_for_lcd() {
  //Setting Up LCD
  lcd.begin(16,2);//Defining 16 columns and 2 rows of lcd display
  lcd.backlight();//To Power ON the back light
  //lcd.backlight();// To Power OFF the back light
}

void setup_for_situp() {
  Wire.begin();                                                        //Start I2C as master
  
  //Activate the MPU-6050
  Wire.beginTransmission(0x68);                                        //Start communicating with the MPU-6050
  Wire.write(0x6B);                                                    //Send the requested starting register
  Wire.write(0x00);                                                    //Set the requested starting register
  Wire.endTransmission();                                             
  
  //Configure the accelerometer (+/-8g)
  Wire.beginTransmission(0x68);                                        //Start communicating with the MPU-6050
  Wire.write(0x1C);                                                    //Send the requested starting register
  Wire.write(0x10);                                                    //Set the requested starting register
  Wire.endTransmission();                                             
  
  //Configure the gyro (500dps full scale)
  Wire.beginTransmission(0x68);                                        //Start communicating with the MPU-6050
  Wire.write(0x1B);                                                    //Send the requested starting register
  Wire.write(0x08);                                                    //Set the requested starting register
  Wire.endTransmission();
  
  for (int cal_int = 0; cal_int < 1000 ; cal_int ++){                  //Read the raw acc and gyro data from the MPU-6050 for 1000 times
    Wire.beginTransmission(0x68);                                        //Start communicating with the MPU-6050
    Wire.write(0x3B);                                                    //Send the requested starting register
    Wire.endTransmission();                                              //End the transmission
    Wire.requestFrom(0x68,14);                                           //Request 14 bytes from the MPU-6050
    while(Wire.available() < 14);                                        //Wait until all the bytes are received
    acc_x = Wire.read()<<8|Wire.read();                                  
    acc_y = Wire.read()<<8|Wire.read();                                  
    acc_z = Wire.read()<<8|Wire.read();                                  
    temp = Wire.read()<<8|Wire.read();                                   
    gyro_x = Wire.read()<<8|Wire.read();                                 
    gyro_y = Wire.read()<<8|Wire.read();                                 
    gyro_z = Wire.read()<<8|Wire.read();
    
    gyro_x_cal += gyro_x;                                              //Add the gyro x offset to the gyro_x_cal variable
    gyro_y_cal += gyro_y;                                              //Add the gyro y offset to the gyro_y_cal variable
    gyro_z_cal += gyro_z;                                              //Add the gyro z offset to the gyro_z_cal variable
    delay(3);                                                          //Delay 3us to have 250Hz for-loop
  }

  // divide by 1000 to get avarage offset
  gyro_x_cal /= 1000;                                                 
  gyro_y_cal /= 1000;                                                 
  gyro_z_cal /= 1000;                                                 
  loop_timer = micros();                                               //Reset the loop timer
}

void setup_for_pushup() {
  pinMode(echoPin, INPUT);
  pinMode(trigPin, OUTPUT);
}

void setup_for_jogging() {
  Wire.begin();
  
  Wire.beginTransmission(0b1101000); //This is the I2C address of the MPU (b1101000/b1101001 for AC0 low/high datasheet sec. 9.2)
  Wire.write(0x6B); //Accessing the register 6B - Power Management (Sec. 4.28)
  Wire.write(0b00000000); //Setting SLEEP register to 0. (Required; see Note on p. 9)
  Wire.endTransmission();  
  Wire.beginTransmission(0b1101000); //I2C address of the MPU
  Wire.write(0x1B); //Accessing the register 1B - Gyroscope Configuration (Sec. 4.4) 
  Wire.write(0x00000000); //Setting the gyro to full scale +/- 250deg./s 
  Wire.endTransmission(); 
  Wire.beginTransmission(0b1101000); //I2C address of the MPU
  Wire.write(0x1C); //Accessing the register 1C - Acccelerometer Configuration (Sec. 4.5) 
  Wire.write(0b00000000); //Setting the accel to +/- 2g
  Wire.endTransmission();
}

void setup_for_led() {
  pinMode(CA_1, OUTPUT);
  pinMode(CA_2, OUTPUT);
  pinMode(CA_3, OUTPUT);
  pinMode(CA_4, OUTPUT);
  pinMode(clk, OUTPUT);
  pinMode(latch, OUTPUT);
  pinMode(data, OUTPUT);
  digitalWrite(CA_1, HIGH);
  digitalWrite(CA_2, HIGH);
  digitalWrite(CA_3, HIGH);
  digitalWrite(CA_4, HIGH);
}

void introduction () {
  lcd.clear();//Clean the screen
  lcd.setCursor(0,0); //Defining positon to write from first row,first column .
  lcd.print("Hello! Wanna"); //You can write 16 Characters per line .
  lcd.setCursor(0,1);  //Defining positon to write from second row,first column .
  lcd.print("Get Healthy?");
  delay(4000); 
    
  lcd.clear();//Clean the screen
  lcd.setCursor(0,0); 
  lcd.print("Let's Workout");
  lcd.setCursor(0,1);
  lcd.print("w/ WorkIt! App!");
  delay(4000);
}

void situp() {
  countWorkOut = 0;
  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Let's do");
  lcd.setCursor(0,1);
  lcd.print(total);
  lcd.print(" Sit Ups!");
  delay(4000);

  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Press the Button");
  lcd.setCursor(0,1);
  lcd.print("to Start!");
  delay(4000);

  while(!light) {
    reading = LOW;
    reading = digitalRead(inPin);
    
    if(reading == HIGH){
      for(int i=0; i<255; i++){
        analogWrite(outPin, i);
        delay(5);
      }
      light = true;
    }
  }

  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Ready?");
  lcd.setCursor(0,1);
  delay(4000);
  lcd.print("Let's Go!");
  delay(4000);

  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Work Out :");
  lcd.setCursor(0,1);
  lcd.print("Do ");
  lcd.print(total);
  lcd.print(" Sit Ups!");
  
  while (countWorkOut < total){
    t.stop(timer_event); //stop timer if anythign to read
    digitalWrite(CA_1, HIGH);
    digitalWrite(CA_2, HIGH);
    digitalWrite(CA_3, HIGH);
    digitalWrite(CA_4, HIGH);
    first_digit = countWorkOut / 100;
    digits[0] = first_digit;
  
    int first_left = countWorkOut - (first_digit * 100);
    second_digit = first_left / 10;
    digits[1] = second_digit;
    int second_left = first_left - (second_digit * 10);
    third_digit = second_left;
    digits[2] = third_digit;
    fourth_digit = second_left - (third_digit * 10);
    digits[3] = 0;
    
    digitalWrite(CA_1, HIGH);
    digitalWrite(CA_2, HIGH);
    digitalWrite(CA_3, HIGH);
    digitalWrite(CA_4, HIGH);
    digitalWrite(latch, LOW); //put the shift register to read
    shiftOut(data, clk, LSBFIRST, numbers[digits[count]]); //send the data
    digitalWrite(CAS[count], LOW); //turn on the relevent digit
    digitalWrite(latch, HIGH); //put the shift register to write mode
    count++; //count up the digit
    if (count == 4) { // keep the count between 0-3
      count = 0;
    }
    delay(1);

    Wire.beginTransmission(0x68);                                        //Start communicating with the MPU-6050
    Wire.write(0x3B);                                                    //Send the requested starting register
    Wire.endTransmission();                                              //End the transmission
    Wire.requestFrom(0x68,14);                                           //Request 14 bytes from the MPU-6050
    while(Wire.available() < 14);                                        //Wait until all the bytes are received
    acc_x = Wire.read()<<8|Wire.read();                                  
    acc_y = Wire.read()<<8|Wire.read();                                  
    acc_z = Wire.read()<<8|Wire.read();                                  
    temp = Wire.read()<<8|Wire.read();                                   
    gyro_x = Wire.read()<<8|Wire.read();                                 
    gyro_y = Wire.read()<<8|Wire.read();                                 
    gyro_z = Wire.read()<<8|Wire.read(); 
  
    //Subtract the offset values from the raw gyro values
    gyro_x -= gyro_x_cal;                                                
    gyro_y -= gyro_y_cal;                                                
    gyro_z -= gyro_z_cal;                                                
           
    //Gyro angle calculations . Note 0.0000611 = 1 / (250Hz x 65.5)
    angle_pitch += gyro_x * 0.0000611;                                   //Calculate the traveled pitch angle and add this to the angle_pitch variable
    angle_roll += gyro_y * 0.0000611;                                    //Calculate the traveled roll angle and add this to the angle_roll variable
    //0.000001066 = 0.0000611 * (3.142(PI) / 180degr) The Arduino sin function is in radians
    angle_pitch += angle_roll * sin(gyro_z * 0.000001066);               //If the IMU has yawed transfer the roll angle to the pitch angel
    angle_roll -= angle_pitch * sin(gyro_z * 0.000001066);               //If the IMU has yawed transfer the pitch angle to the roll angel
    
    //Accelerometer angle calculations
    acc_total_vector = sqrt((acc_x*acc_x)+(acc_y*acc_y)+(acc_z*acc_z));  //Calculate the total accelerometer vector
    //57.296 = 1 / (3.142 / 180) The Arduino asin function is in radians
    angle_pitch_acc = asin((float)acc_y/acc_total_vector)* 57.296;       //Calculate the pitch angle
    angle_roll_acc = asin((float)acc_x/acc_total_vector)* -57.296;       //Calculate the roll angle
    
    angle_pitch_acc -= 0.0;                                              //Accelerometer calibration value for pitch
    angle_roll_acc -= 0.0;                                               //Accelerometer calibration value for roll
  
    if(set_gyro_angles){                                                 //If the IMU is already started
      angle_pitch = angle_pitch * 0.9996 + angle_pitch_acc * 0.0004;     //Correct the drift of the gyro pitch angle with the accelerometer pitch angle
      angle_roll = angle_roll * 0.9996 + angle_roll_acc * 0.0004;        //Correct the drift of the gyro roll angle with the accelerometer roll angle
    }
    else{                                                                //At first start
      angle_pitch = angle_pitch_acc;                                     //Set the gyro pitch angle equal to the accelerometer pitch angle 
      angle_roll = angle_roll_acc;                                       //Set the gyro roll angle equal to the accelerometer roll angle 
      set_gyro_angles = true;                                            //Set the IMU started flag
    }
    
    //To dampen the pitch and roll angles a complementary filter is used
    angle_pitch_output = angle_pitch_output * 0.9 + angle_pitch * 0.1;   //Take 90% of the output pitch value and add 10% of the raw pitch value
    angle_roll_output = angle_roll_output * 0.9 + angle_roll * 0.1;      //Take 90% of the output roll value and add 10% of the raw roll value
    
    if (angle_pitch_output * 6 >= 60) {
      if (isStartSitUp) {
        isStartSitUp = false;  
      }
    } else if (angle_pitch_output * 6 <= 30) {
      if (!isStartSitUp) {
        isStartSitUp = true;
        countWorkOut = countWorkOut + 1;
      }
    }
    
    Serial.print(" | count  = "); Serial.print(countWorkOut); Serial.print(" | Angle  = "); Serial.println(angle_pitch_output * 6);
  
    while(micros() - loop_timer < 4000);                                 //Wait until the loop_timer reaches 4000us (250Hz) before starting the next loop
    loop_timer = micros();//Reset the loop timer
  }

  delay(2000);
  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("YOU DID IT!");
  lcd.setCursor(0,1);
  lcd.print("GOOD JOB!");
  delay(5000);

  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Press the Button");
  lcd.setCursor(0,1);
  lcd.print("to Show Result!");

  while(light) {
    reading = LOW;
    reading = digitalRead(inPin);
    
    if(reading == HIGH){
      for(int i=255; i>0; i--){
        analogWrite(outPin, i);
        delay(5);
      }
      light = false;
    }
  }
}

void pushup() {
  countWorkOut = 0;
  light = false;
  
  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Let's do");
  lcd.setCursor(0,1);
  lcd.print(total);
  lcd.print(" Push Ups!");
  delay(4000);

  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Press the Button");
  lcd.setCursor(0,1);
  lcd.print("to Start!");

  while(!light) {
    reading = LOW;
    reading = digitalRead(inPin);
    
    if(reading == HIGH){
      Serial.print("huhu");
      for(int i=0; i<255; i++){
        analogWrite(outPin, i);
        delay(5);
      }
      light = true;
    }
  }

  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Ready?");
  lcd.setCursor(0,1);
  delay(4000);
  lcd.print("Let's Go!");
  delay(4000);

  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Work Out :");
  lcd.setCursor(0,1);
  lcd.print("Do ");
  lcd.print(total);
  lcd.print(" Push Ups!");
  
  while (countWorkOut < total) {
    t.stop(timer_event); //stop timer if anythign to read
    digitalWrite(CA_1, HIGH);
    digitalWrite(CA_2, HIGH);
    digitalWrite(CA_3, HIGH);
    digitalWrite(CA_4, HIGH);
    first_digit = countWorkOut / 100;
    digits[0] = first_digit;
  
    int first_left = countWorkOut - (first_digit * 100);
    second_digit = first_left / 10;
    digits[1] = second_digit;
    int second_left = first_left - (second_digit * 10);
    third_digit = second_left;
    digits[2] = third_digit;
    fourth_digit = second_left - (third_digit * 10);
    digits[3] = 0;
    
    digitalWrite(CA_1, HIGH);
    digitalWrite(CA_2, HIGH);
    digitalWrite(CA_3, HIGH);
    digitalWrite(CA_4, HIGH);
    digitalWrite(latch, LOW); //put the shift register to read
    shiftOut(data, clk, LSBFIRST, numbers[digits[count]]); //send the data
    digitalWrite(CAS[count], LOW); //turn on the relevent digit
    digitalWrite(latch, HIGH); //put the shift register to write mode
    count++; //count up the digit
    if (count == 4) { // keep the count between 0-3
      count = 0;
    }
    delay(1);
    
    digitalWrite(trigPin, LOW);
    delayMicroseconds(2);
    
    digitalWrite(trigPin, HIGH);
    delayMicroseconds(10);
    
    digitalWrite(trigPin, LOW);
    duration = pulseIn(echoPin, HIGH);
    distance = int(duration/58.2) % 100;

    if (distance >= 35) {
      if (isStartPushUp) {
        isStartPushUp = false;  
      }
    } else if (distance <= 10) {
      if (!isStartPushUp) {
        isStartPushUp = true;
        countWorkOut = countWorkOut + 1;
      }
    }

    Serial.print(" | count  = "); Serial.print(countWorkOut); Serial.print(" | Jarak  = "); Serial.println(distance);
  }

  delay(2000);
  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("YOU DID IT!");
  lcd.setCursor(0,1);
  lcd.print("GOOD JOB!");
  delay(5000);

  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Press the Button");
  lcd.setCursor(0,1);
  lcd.print("to Show Result!");

  while(light) {
    reading = LOW;
    reading = digitalRead(inPin);
    
    if(reading == HIGH){
      Serial.print("huhu");
      for(int i=255; i>0; i--){
        analogWrite(outPin, i);
        delay(5);
      }
      light = false;
    }
  }
}

void jogging() {
  countWorkOut = 0;
  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Let's do");
  lcd.setCursor(0,1);
  lcd.print(total);
  lcd.print(" Steps!");
  delay(4000);

  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Press the Button");
  lcd.setCursor(0,1);
  lcd.print("to Start!");

  while(!light) {
    reading = LOW;
    reading = digitalRead(inPin);
    
    if(reading == HIGH){
      Serial.print("huhu");
      for(int i=0; i<255; i++){
        analogWrite(outPin, i);
        delay(5);
      }
      light = true;
    }
  }
  
  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Ready?");
  lcd.setCursor(0,1);
  delay(4000);
  lcd.print("Let's Go!");
  delay(4000);

  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Work Out :");
  lcd.setCursor(0,1);
  lcd.print("Jog ");
  lcd.print(total);
  lcd.print(" Steps!");

  while (countWorkOut < total) {
    t.stop(timer_event); //stop timer if anythign to read
    digitalWrite(CA_1, HIGH);
    digitalWrite(CA_2, HIGH);
    digitalWrite(CA_3, HIGH);
    digitalWrite(CA_4, HIGH);
    first_digit = countWorkOut / 100;
    digits[0] = first_digit;
  
    int first_left = countWorkOut - (first_digit * 100);
    second_digit = first_left / 10;
    digits[1] = second_digit;
    int second_left = first_left - (second_digit * 10);
    third_digit = second_left;
    digits[2] = third_digit;
    fourth_digit = second_left - (third_digit * 10);
    digits[3] = 0;
    
    digitalWrite(CA_1, HIGH);
    digitalWrite(CA_2, HIGH);
    digitalWrite(CA_3, HIGH);
    digitalWrite(CA_4, HIGH);
    digitalWrite(latch, LOW); //put the shift register to read
    shiftOut(data, clk, LSBFIRST, numbers[digits[count]]); //send the data
    digitalWrite(CAS[count], LOW); //turn on the relevent digit
    digitalWrite(latch, HIGH); //put the shift register to write mode
    count++; //count up the digit
    if (count == 4) { // keep the count between 0-3
      count = 0;
    }
    delay(1);
    
    Wire.beginTransmission(0b1101000); //I2C address of the MPU
    Wire.write(0x3B); //Starting register for Accel Readings
    Wire.endTransmission();
    Wire.requestFrom(0b1101000,6); //Request Accel Registers (3B - 40)
    while(Wire.available() < 6);
    accelX = Wire.read()<<8|Wire.read(); //Store first two bytes into accelX
    accelY = Wire.read()<<8|Wire.read(); //Store middle two bytes into accelY
    accelZ = Wire.read()<<8|Wire.read(); //Store last two bytes into accelZ
    
    gForceX = accelX / 16384.0;
    gForceY = accelY / 16384.0; 
    gForceZ = accelZ / 16384.0;
  
    Wire.beginTransmission(0b1101000); //I2C address of the MPU
    Wire.write(0x43); //Starting register for Gyro Readings
    Wire.endTransmission();
    Wire.requestFrom(0b1101000,6); //Request Gyro Registers (43 - 48)
    while(Wire.available() < 6);
    gyroX = Wire.read()<<8|Wire.read(); //Store first two bytes into accelX
    gyroY = Wire.read()<<8|Wire.read(); //Store middle two bytes into accelY
    gyroZ = Wire.read()<<8|Wire.read(); //Store last two bytes into accelZ
   
    rotX = gyroX / 131.0;
    rotY = gyroY / 131.0; 
    rotZ = gyroZ / 131.0;
  
    if (gForceY > 0.3) {
      flag = 1;
  //    Serial.println("inside greater, flag = 1");
      previousMillis = millis();
      currentMillis = millis();
    }
  
    if ((currentMillis - previousMillis <= interval) && (flag)) {
  //    Serial.println("inside time loop");
    
  
      if (gForceY < -0.3) {
        countWorkOut++;
        flag = 0;
    
  //      Serial.println("inside lesser, flag = 0");
      }
    }
  
    currentMillis = millis();
  
    if (currentMillis - previousMillis > interval) {
      flag = 0;
  //    Serial.println("inside clear, flag = 0");
    }
  
    Serial.print("Steps = "); Serial.print(countWorkOut); Serial.print("| Y = "); Serial.println(gForceY);

  }
  
  delay(2000);
  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("YOU DID IT!");
  lcd.setCursor(0,1);
  lcd.print("GOOD JOB!");
  delay(5000);

  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Press the Button");
  lcd.setCursor(0,1);
  lcd.print("to Show Result!");

  while(light) {
    reading = LOW;
    reading = digitalRead(inPin);
    
    if(reading == HIGH){
      Serial.print("huhu");
      for(int i=255; i>0; i--){
        analogWrite(outPin, i);
        delay(5);
      }
      light = false;
    }
  }
}

void complete() {
  countWorkOut = 0;
  
  delay(4000);
  lcd.clear();
  lcd.setCursor(0,0);
  lcd.print("You burned : ");
  lcd.setCursor(0,1);
  lcd.print(calories_data);
  lcd.print(" kcal");
  
  delay(2000);
  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("AWESOME WORKOUT!");
  lcd.setCursor(0,1);
  lcd.print("See Ya!!!");
  delay(20000);
}

void serial_com(){
  Serial.println(workout_type+","+total_workout+","+weight);
  if (Serial.available() > 0) {
    calories = Serial.readString();
    Serial.println(calories);
  }
}

void read_bt(){

  lcd.clear();
  lcd.setCursor(0,0); 
  lcd.print("Waiting for data");
  lcd.setCursor(0,1);
  lcd.print("from Android");
  
  workout_type = Serial.readStringUntil('.');
  total_workout = Serial.readStringUntil(',');
  weight = Serial.readStringUntil('^');
  calories_data = Serial.readStringUntil('&');
  total = total_workout.toInt();

//  Serial.println("workout Type : "+ workout_type);
//  Serial.println("total : "+ total_workout);
//  Serial.println("weight : "+ weight);
//  Serial.println("calories : "+ calories_data);
//  Serial.println("delay tiap 1 detik");
}
