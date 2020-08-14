package co.aresid.book13.recyclerview


/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */


class TrackingListClickListener(val clickListener: (position: Int) -> Unit) {

    fun onClick(position: Int) = clickListener(position)

}