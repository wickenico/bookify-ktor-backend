package com.nw.utils

import com.nw.persistence.BookFacade
import com.nw.persistence.BookRepository
import com.nw.persistence.BookTagFacade
import com.nw.persistence.BookTagRepository
import com.nw.persistence.FavoriteFacade
import com.nw.persistence.FavoriteRepository
import com.nw.persistence.TagFacade
import com.nw.persistence.TagRepository
import com.nw.persistence.UserFacade
import com.nw.persistence.UserRepository

val favoriteFacade: FavoriteFacade = FavoriteRepository()
val bookFacade: BookFacade = BookRepository()
val bookTagFacade: BookTagFacade = BookTagRepository()
val tagFacade: TagFacade = TagRepository()
val userFacade: UserFacade = UserRepository()
