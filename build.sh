rm -vf key-manager-rest-docker.zip
zip -r key-manager-rest-docker.zip *
aws s3 cp key-manager-rest-docker.zip s3://pix-bucket-31032021/