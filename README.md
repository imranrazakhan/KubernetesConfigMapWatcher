# KubernetesConfigMapWatcher
Apache Camel Route to Watch Kubernetes ConfigMap Changes


SELINUX=permissive

yum install -y  wget git zile nano net-tools docker-1.13.1 bind-utils iptables-services bridge-utils bash-completion kexec-tools sos psacct openssl-devel httpd-tools NetworkManager python-cryptography python2-pip python-devel  python-passlib java-1.8.0-openjdk-headless "@Development Tools"

yum install -y centos-release-openshift-origin311

yum -y install epel-release

# Disable the EPEL repository globally so that is not accidentally used during later steps of the installation
sed -i -e "s/^enabled=1/enabled=0/" /etc/yum.repos.d/epel.repo


mkdir -p /etc/origin/master/
touch /etc/origin/master/htpasswd

htpasswd -b /etc/origin/master/htpasswd admin imran
oc adm policy add-cluster-role-to-user cluster-admin admin

ansible all -m shell -a "systemctl restart dnsmasq"

ssh-keygen
ssh-copy-id -i ~/.ssh/mykey user@host


new_nodes

[new_nodes]
okd2.ycube.local openshift_node_group_name="node-config-all-in-one" openshift_schedulable=true

ansible-playbook -i inventory.ini openshift-ansible/playbooks/

rm -rf /etc/etcd /var/lib/etcd /etc/origin /root/.kube/ /var/log/journal/*

Playbook Name	File Location
Health Check

/usr/share/ansible/openshift-ansible/playbooks/openshift-checks/pre-install.yml

Node Bootstrap

/usr/share/ansible/openshift-ansible/playbooks/openshift-node/bootstrap.yml

etcd Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-etcd/config.yml

NFS Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-nfs/config.yml

Load Balancer Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-loadbalancer/config.yml

Master Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-master/config.yml

Master Additional Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-master/additional_config.yml

Node Join

/usr/share/ansible/openshift-ansible/playbooks/openshift-node/join.yml

GlusterFS Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-glusterfs/config.yml

Hosted Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-hosted/config.yml

Monitoring Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-monitoring/config.yml

Web Console Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-web-console/config.yml

Admin Console Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-console/config.yml

Metrics Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-metrics/config.yml

metrics-server

/usr/share/ansible/openshift-ansible/playbooks/metrics-server/config.yml

Logging Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-logging/config.yml

Availability Monitoring Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-monitor-availability/config.yml

Service Catalog Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-service-catalog/config.yml

Management Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-management/config.yml

Descheduler Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-descheduler/config.yml

Node Problem Detector Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-node-problem-detector/config.yml

Autoheal Install

/usr/share/ansible/openshift-ansible/playbooks/openshift-autoheal/config.yml

Operator Lifecycle Manager (OLM) Install (Technology Preview)

/usr/share/ansible/openshift-ansible/playbooks/olm/con

