package co.aresid.book13.database

/**
 *    Created on: 02.09.2020
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class DatabaseNames {

    object Database {

        const val NAME = "Book13Database"

    }

    object Table {

        object BookData {

            const val NAME = "book_data"

            object Column {

                const val ID = "id"

                const val TITLE = "title"

                const val AUTHOR = "author"

                const val NUMBER_OF_PAGES = "number_of_pages"

                const val START_DATE = "start_date"

                const val FINISH_DATE = "finish_date"

            }

        }

        object TrackingData {

            const val NAME = "tracking_data"

            object Column {

                const val ID = "id"

                const val BOOK_ID = "book_id"

                const val BOOK_TITLE = "book_title"

                const val START_PAGE_COUNT = "start_page_count"

                const val FINISH_PAGE_COUNT = "finish_page_count"

                const val START_DATE = "start_date"

                const val FINISH_DATE = "finish_date"

            }

        }

    }

}