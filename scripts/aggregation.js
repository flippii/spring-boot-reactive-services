db
    .getCollection('user-follow')
    .aggregate([
        { $match: { userId: "1" } },
        { $lookup: {
                from: "article-favourite",
                localField: "followId",
                foreignField: "userId",
                as: "favourite"
            }
        },
        { $unwind: { path: "$favourite", preserveNullAndEmptyArrays: true } },
        { $project: {
                userId: "$userId",
                followId: "$followId",
                articleId: "$favourite.articleId"
            }
        },
        { $match: { articleId: { $ne: null } } },
        { $lookup: {
                from: "articles",
                localField: "articleId",
                foreignField: "articleId",
                as: "article"
            }
        },
        { $addFields: { articleSize: { $size: '$article' } } },
        { $match: { articleSize: { $gte: 1 } } },
        { $unwind: { path: "$article", preserveNullAndEmptyArrays: true } },
        { $lookup: {
                from: "article-favourite",
                localField: "articleId",
                foreignField: "articleId",
                as: "article-favourite"
            }
        },
        { $lookup: {
                from: "article-favourite",
                let: { user_id: "$userId", article_id: "$articleId" },
                pipeline: [
                    { $match:
                            { $expr:
                                    { $and:
                                            [
                                                { $eq: [ "$userId", "$$user_id" ] },
                                                { $eq: [ "$articleId", "$$article_id" ] }
                                            ]
                                    }
                            }
                    }
                ],
                as: "user-favourite"
            }
        },
        {
            $addFields: {
                "article.favouriteCount": { $size: "$article-favourite" },
                "article.favouritedCount": { $size: "$user-favourite" }
            }
        },
        {
            $group: {
                _id: { userId: "$userId", followId: "$followId" },
                article: { $push: "$article" },
                followIds: { $push: "$followId"  }
            }
        },
        { $unwind: { path: "$article", preserveNullAndEmptyArrays: true } },
        { $lookup: {
                from: "user-follow",
                let: { user_id: "$_id.userId", follow_ids: "$followIds" },
                pipeline: [
                    { $match:
                            { $expr:
                                    { $and:
                                            [
                                                { $eq: [ "$userId",  "$$user_id" ] },
                                                { $in: [ "$followId", "$$follow_ids" ] }
                                            ]
                                    }
                            }
                    }
                ],
                as: "followsAuthor"
            }
        },
        {
            $addFields: {
                "article.isFollowsAuthorCount": { $size: "$followsAuthor" }
            }
        },
        { $project: {
                _id: "$article._id",
                userId: "$_id.userId",
                followId: "$_id.followId",
                articleId: "$article.articleId",
                slug: "$article.slug",
                title: "$article.title",
                description: "$article.description",
                body: "$article.body",
                tags: "$article.tags",
                favouriteCount: "$article.favouriteCount",
                favourited: { $cond: { if: { $gte: [ "$article.favouritedCount", 1 ] }, then: 1, else: 0 } },
                isFollowsAuthor: { $cond: { if: { $gte: [ "$article.isFollowsAuthorCount", 1 ] }, then: 1, else: 0 } }
            }
        }
    ])
