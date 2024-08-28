PREREQUITES

Please install the following packges,
    1. Packer
    2. Virtualbox

There are two projects in this repo,

    1. rhel_openstack: used to create openstack image with packer
    2. rhel_vm_and_openstack: used to create virtualbox based image or openstack based image. Recommended to use this.

BUILD virtualbox based image

It depends on the base image from rhel repo, please create the base image first and the follow the instruction here,

     1. Go to the project folder,
         cd rhel_vm_and_openstack
     2. Remove current output,
         rm -rf output*     
     3. change the aia-sandbox.json variable to point to the base image     
     4. Run the packer build, this step might take 1hr or more, have a coffee and then come back
        packer build --only=virtualbox-ovf -var 'user_name=vagrant' -var 'group_name=vagrant' -var 'home_directory=/home/vagrant' aia-sandbox.json

BUILD openstack based image

If you build against your openstack cloud, please change the openstack builder variables to be aligned with your building openstack cluster. 

     1. Go to the project folder.
         cd rhel_vm_and_openstack
     2. Download the openstack API access file from openstack dashboard(Compute->Access&Security->API Access) and source it. prefer to use v2 version.
         source <API access file>. 
         e.g, source aia-project-openrc.sh
     3. In packer_conf folder there are files for ITTE and DEV openstack cloud. Please create a new file for your openstack environment. Please check packer documentation for details.
        (https://www.packer.io/docs/builders/openstack.html)

       Belows are brief description of the file content(// is used as comment identifier by this README, it's not supported in the actual file),

       {
        "openstack_flavor": "aia.flavor1",   //flavor used for launching source_image based instance
        "openstack_source_image": "cd0c6305-a9ac-4fb8-a79c-919269798405", //redhat based source image should be used here as the installation is redhat based. You can use the image from ENM(https://cifwk-oss.lmera.ericsson.se/prototype/packages/ERICrhel7baseimage_CXP9032719/)
        "openstack_networks": "a0a29db1-2601-4405-93a1-86724f50ce9b",  //network being used to launch source_image based instance
        "openstack_identity_endpoint":"http://10.44.149.3:5000/v3", //As defined in the API access file
        "openstack_security_groups":"aia", //security group being used to launch source_image based instance
        "openstack_floating_ip_pool":"admin_floating_net", //Floating IP pool when launching source_image based instance, you can use fixed IP as well, check the documentation.
       }

     4. Run the packer build, this step might take 1hr or more, have a coffee and then come back.
        packer build --only=openstack -env-file=packer_conf/itte_packer_env.conf aia-sandbox.json
     5. Follow the step in https://github.com/fjammes/packer-openstack-example to convert raw image to qcow2 to save space and ease transfer
     6. Launch the new instance with generated image, and the aia_user_data file is required as the user data.
