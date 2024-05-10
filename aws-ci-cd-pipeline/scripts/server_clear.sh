sudo rm -rf /home/ec2-user/server

{ which java; } || { sudo yum install java-17-amazon-corretto-headless.x86_64 -y; }