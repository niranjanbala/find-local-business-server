/*
 * File: app/view/MatchDisplay.js
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

Ext.define('MyApp.view.MatchDisplay', {
    extend: 'Ext.Panel',
    alias: 'widget.matchDisplay',

    requires: [
        'MyApp.view.MatchInfoPanel'
    ],

    config: {
        style: 'background-color: gray',
        layout: {
            type: 'vbox'
        },
        items: [
            {
                xtype: 'matchinfopanel',
                height: 181,
                id: 'matchInfoPanel'
            },
            {
                xtype: 'label'
            },
            {
                xtype: 'selectfield',
                id: 'winner',
                label: 'WINNER'
            },
            {
                xtype: 'selectfield',
                id: 'marginWin',
                label: 'MARGIN'
            },
            {
                xtype: 'tabpanel',
                docked: 'bottom',
                style: 'padding-top:5px; padding-bottom:5px',
                items: [
                    {
                        xtype: 'container',
                        title: 'About'
                    }
                ]
            },
            {
                xtype: 'toolbar',
                docked: 'top',
                style: 'padding-top:5px; padding-bottom:5px',
                items: [
                    {
                        xtype: 'button',
                        width: 148,
                        icon: 'http://find-business.appspot.com/ipl/images/previous48.png',
                        text: 'BACK'
                    },
                    {
                        xtype: 'button',
                        width: 146,
                        icon: 'http://find-business.appspot.com/ipl/images/next48.png',
                        iconAlign: 'right',
                        text: 'NEXT'
                    }
                ]
            }
        ]
    },

    getMatchInfoPanel: function() {
        return Ext.ComponentQuery.query(this.observableId +' > #matchInfoPanel')[0];
    },

    getWinnerSelectField: function() {
        return Ext.ComponentQuery.query(this.observableId +' > #winner')[0];
    },

    getValueString: function() {
        var team1=this.getMatchInfoPanel().team1;
        var team2=this.getMatchInfoPanel().team2;
        var winner=this.getWinnerSelectField().getValue();
        var margin=this.getMarginWinSelectField().getValue();
        var line=team1+','+team2+','+winner+','+margin;
        return line;
    },

    getNavToolbar: function() {

    },

    getMarginWinSelectField: function() {
        return Ext.ComponentQuery.query(this.observableId +' > #marginWin')[0];
    }

});