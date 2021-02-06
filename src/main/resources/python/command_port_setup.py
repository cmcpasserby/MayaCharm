import maya.cmds as cmds

if not cmds.commandPort(":{0,number,#}", query=True):
    cmds.commandPort(name=":{0,number,#}")
