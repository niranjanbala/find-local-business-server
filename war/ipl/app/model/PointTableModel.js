/*
 * File: app/model/PointTableModel.js
 *
 * This file was generated by Sencha Architect version 2.1.0.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.1.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.1.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('MyApp.model.PointTableModel', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'teamName'
        },
        {
            name: 'matches',
            type: 'int'
        },
        {
            name: 'won',
            type: 'int'
        },
        {
            name: 'lost',
            type: 'int'
        },
        {
            name: 'nr',
            type: 'int'
        },
        {
            name: 'netrr',
            type: 'float'
        },
        {
            name: 'tied',
            type: 'int'
        },
        {
            name: 'points',
            type: 'int'
        },
        {
            name: 'forOver',
            type: 'float'
        },
        {
            name: 'forRuns',
            type: 'int'
        },
        {
            name: 'againstOver',
            type: 'float'
        },
        {
            name: 'againstRuns',
            type: 'int'
        },
        {
            name: 'chances',
            type: 'float'
        }
    ]
});