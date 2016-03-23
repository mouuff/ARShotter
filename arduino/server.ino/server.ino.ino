#include <WiFi.h>
#include <WiFiUdp.h>
#include <Servo.h>

char ssid[] = "android";
char pass[] = "android42";
int status = WL_IDLE_STATUS;

int localPort = 12345;

char packetBuffer[255];

WiFiUDP Udp;

char ReplyBuffer[] = "pong";

Servo ServoLeft;
Servo ServoRight;


void setup() {
  Serial.begin(9600);
  
  ServoLeft.attach(9);
  ServoRight.attach(6);
  
  ServoLeft.write(90);
  ServoRight.write(90);
  
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    while(true);
  }
  Serial.println(WiFi.firmwareVersion());
  while ( status != WL_CONNECTED) { 
    Serial.print("Attempting to connect to WPA SSID: ");
    Serial.println(ssid);
    status = WiFi.begin(ssid, pass);
    delay(5000);
  }
  
  Serial.println(WiFi.localIP());

  if (Udp.begin(localPort)){
    Serial.println("Now listenning on ");
    Serial.println(localPort);
  }
  else{
    Serial.println("Socket unavaiable");
  } 
}

void loop() {
  int packetSize = Udp.parsePacket();
  if (packetSize){
    
    int len = Udp.read(packetBuffer, 255);
    if (len > 0) packetBuffer[len] = 0;
    
    if (packetBuffer[0] == 'L'){
      String buff = String(packetBuffer);
      Serial.println("left");
      ServoLeft.write(0);
      delay(1000);
      ServoLeft.write(90);
    }
    if (packetBuffer[0] == 'R'){
      String buff = String(packetBuffer);
      Serial.println("right");
      ServoRight.write(0);
      delay(1000);
      ServoRight.write(90);
    }
  }
}
