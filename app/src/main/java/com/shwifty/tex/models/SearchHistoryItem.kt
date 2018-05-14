package com.shwifty.tex.models

data class SearchHistoryItem(val searchTerm: String, val time: Long = System.currentTimeMillis())