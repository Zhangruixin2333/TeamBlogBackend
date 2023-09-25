# 建表脚本
#
#

-- 创建库
create database if not exists team_blog;

-- 切换库
use team_blog;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';

-- 用户博文表
create table if not exists `user_blog`
(
    `id` bigint(20) not null auto_increment comment '博文id' primary key,
    `userId` bigint(20) not null comment '用户id',
    `title` varchar(256) not null comment '博文标题',
    `description` varchar(256) not null comment '博文描述',
    `content` longtext not null comment '博文内容',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint(4) default '0' not null comment '逻辑删除（0-未删除，1-已删除）'
) comment '用户博文表';

-- region 模拟数据
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('7', '674', '义乌', '7iKhr', 'lCH7G', '2022-09-18 04:34:00', '2022-07-29 20:20:14');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('234588', '2892874766', '揭阳', 'ifKhX', 'hi0', '2022-12-31 04:37:27', '2022-06-13 23:40:50');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('8', '6647884567', '富阳', 'GNy4f', 'lduKg', '2022-02-16 14:51:06', '2022-05-19 06:32:15');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('3', '246', '盘锦', '4Mh', 'klnG4', '2022-08-17 06:46:54', '2022-04-04 08:17:50');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('9297865749', '68738', '株洲', 'MI6kH', 'kmJc9', '2022-04-05 10:58:17', '2022-01-15 07:55:16');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('201', '325', '乳山', 'tsob', 'dIjh', '2022-11-19 11:07:36', '2022-07-16 21:05:00');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('16471', '58974925', '芜湖', 'yJ', 'IFh6X', '2022-05-15 07:12:52', '2022-05-18 14:36:28');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('37', '6', '天津', 'HVa', 'fXPV', '2022-09-28 19:42:59', '2022-06-27 04:59:11');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('76520528', '31631505', '湛江', 'hxy', '5UzN', '2022-04-22 21:52:49', '2022-02-01 22:29:37');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('6', '282560282', '阳江', 'zR0R', 'zP', '2022-12-25 03:47:49', '2022-09-15 04:06:41');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('54552', '8865353297', '盐城', 'EnH91', 'Ib', '2022-05-27 17:41:48', '2022-10-29 06:24:49');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('6297546438', '153002', '莱西', 'BoUA', 'IL8O', '2022-09-30 09:11:14', '2022-04-27 21:25:22');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('4693286290', '9', '太仓', 'tc9H', 'fnvZ', '2022-11-20 10:40:02', '2022-04-01 00:29:06');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('34', '135003', '嘉峪关', '6yRh8', 'QMEy', '2022-02-03 04:02:04', '2022-11-01 14:21:32');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('8593675219', '7', '唐山', 'VS', 'S8fo1', '2022-05-07 19:36:11', '2022-10-14 04:43:07');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('581961341', '81221489', '平度', 'ayar', 'x82', '2022-11-28 11:47:22', '2022-05-07 09:51:24');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('2719379', '2', '丽水', 'kL', '7a', '2022-05-17 06:43:34', '2022-02-15 07:26:15');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('17999943', '82731408', '南昌', 'XEAEG', 'Hp', '2022-12-29 23:56:15', '2022-06-29 16:40:03');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('39202075', '73', '文登', 'LL', 'I9BAN', '2022-10-18 04:43:51', '2022-06-12 01:13:30');
insert into `user_blog` (`id`, `userId`, `title`, `description`, `content`, `createTime`, `updateTime`) values ('47', '202528', '盘锦', 'FELi', 'kj', '2022-06-07 16:25:29', '2022-08-22 17:59:43');
-- endregion