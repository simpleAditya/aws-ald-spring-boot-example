# product-service

![Capture](https://user-images.githubusercontent.com/25712816/92306201-ef826380-efaa-11ea-9704-5304319e0517.PNG)

CodeDeploy (EC2 User data)

#!/bin/bash
sudo yum -y update
sudo yum -y install ruby
sudo yum -y install wget
cd /home/ec2-user
wget https://aws-codedeploy-ap-south-1.s3.ap-south-1.amazonaws.com/latest/install
sudo chmod +x ./install
sudo ./install auto
sudo yum install -y python-pip
sudo pip install awscli

IAM Roles (EC2 Instance rule)

AmazonEC2RoleforAWSCodeDeploy
AmazonS3FullAccess
AWSCodeDeployRole

SG (EC2)

CustomTCP - 8080 - 0.0.0.0/0

Reference.
https://www.youtube.com/watch?v=531i-n5FMRY
