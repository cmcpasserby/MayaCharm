import maya.cmds as cmds

if not cmds.commandPort(':%1$s', query=True):
    cmds.commandPort(name=':%1$s')