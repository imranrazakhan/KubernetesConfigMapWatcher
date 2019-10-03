#!/bin/bash

export DOMAIN=okd1.ycube.local
export IP=192.168.0.146

# install updates
yum update -y

yum install -y  wget git zile nano net-tools docker-1.13.1 bind-utils iptables-services bridge-utils \
bash-completion kexec-tools sos psacct openssl-devel httpd-tools NetworkManager python-cryptography \
python2-pip python-devel  python-passlib java-1.8.0-openjdk-headless "@Development Tools"

yum install -y centos-release-openshift-origin311

#install epel
yum -y install epel-release

# Disable the EPEL repository globally so that is not accidentally used during later steps of the installation
sed -i -e "s/^enabled=1/enabled=0/" /etc/yum.repos.d/epel.repo

systemctl | grep "NetworkManager.*running" 
if [ $? -eq 1 ]; then
	systemctl start NetworkManager
	systemctl enable NetworkManager
fi

# install the packages for Ansible
yum -y --enablerepo=epel install pyOpenSSL

curl -o ansible.rpm https://releases.ansible.com/ansible/rpm/release/epel-7-x86_64/ansible-2.6.5-1.el7.ans.noarch.rpm
yum -y --enablerepo=epel install ansible.rpm

[ ! -d openshift-ansible ] && git clone https://github.com/openshift/openshift-ansible.git -b release-3.11 --depth=1

cat <<EOD > /etc/hosts
${IP}	    okd1.ycube.local console console.okd1.ycube.local
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4 
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6 
EOD

systemctl restart docker
systemctl enable docker

if [ ! -f /root/.ssh/id_rsa ]; then
	ssh-keygen -q -f /root/.ssh/id_rsa -N ""
	cat /root/.ssh/id_rsa.pub >> /root/.ssh/authorized_keys
	ssh -o StrictHostKeyChecking=no root@$IP "pwd" < /dev/null
fi

mkdir -p /etc/origin/master/
touch /etc/origin/master/htpasswd

ansible-playbook -i inventory.ini openshift-ansible/playbooks/prerequisites.yml
ansible-playbook -i inventory.ini openshift-ansible/playbooks/deploy_cluster.yml

htpasswd -b /etc/origin/master/htpasswd admin imran
oc adm policy add-cluster-role-to-user cluster-admin admin
