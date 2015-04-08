package Protocol;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Merijn
 */
public enum MessageType {
    createAcount,
    login,
    loginReply,
    logout,
    messageTo,
    messageFrom,
    newCalamitie,
    addInfo,
    requestCalamities,
    requestCalamity,
    requestInfo,
    requestFile,
    sendLocation,
    locationReply,
    requestStations,
    addStation,
    logAction,
    requestActionsLog,
}
