# MayaCharm
Maya integration with run and debug configurations for Maya. MayaCharm lets you execute the current document or
arbitrary code as if it was in Maya from PyCharm. For users of the professional version of PyCharm it will also setup
and connect the pydev debugger to Maya as well.

For those just wanting a installable *.jar file of the plugin, you can get the latest stable one from here.
http://rightsomegoodgames.ca/scripts/MayaCharm.jar

## Installation 
It requires some minimal setup. The settings panel is in “Settings > Other Settings > MayaCharm” just define your port number
or leave the default and be sure to copy and paste the code it generates into Maya’s userSetup.py

![MayaCharm Settings Panel](http://rightsomegoodgames.ca/assets/images/MayaCharm/MCSettingsPanel.PNG)


## Usage
If using Community Edition just setup the MayaCharm Runner run Configuration than it will work just like a normal python run configuration.
If using Professional  Edition you can use the MayaCharm debugger instead which can be used as a runner or debugger.
There is also a Execute Selection and a Execute Documeant actions in the run menu, that can also be accessed via alt+s and alt+a

![MayaCharm Debugger Panel](http://rightsomegoodgames.ca/assets/images/MayaCharm/MCDebuggerConfig.PNG)
