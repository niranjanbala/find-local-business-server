/*
 * File: app/view/MatchInfoPanel.js
 *
 * This file was generated by Sencha Architect version 2.1.0.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Sencha Touch 2.1.x library, under independent license.
 * License of Sencha Architect does not include license for Sencha Touch 2.1.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('MyApp.view.MatchInfoPanel', {
    extend: 'Ext.Panel',
    alias: 'widget.matchinfopanel',

    config: {
        style: 'background-color: gray',
        layout: {
            type: 'hbox'
        },
        items: [
            {
                xtype: 'image',
                height: 150,
                width: 140,
                src: 'http://find-business.appspot.com/ipl/logos/Chennai_Super_Kings.png'
            },
            {
                xtype: 'label',
                height: 140,
                html: 'V',
                style: 'padding-left: 10px; padding-right: 10px; padding-top: 60px;',
                width: 34
            },
            {
                xtype: 'image',
                height: 150,
                width: 140,
                src: 'http://find-business.appspot.com/ipl/logos/Chennai_Super_Kings.png'
            }
        ]
    },

    updateView: function() {
        this.getItems().items[0].setSrc('http://find-business.appspot.com/ipl/logos/'+this.team1+'.png');
        this.getItems().items[2].setSrc('http://find-business.appspot.com/ipl/logos/'+this.team2+'.png');
    }

});