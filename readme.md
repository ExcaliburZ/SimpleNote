#极简笔记  
  

##要求
本课题主要设计一种基于Android平台的备忘录，系统主要的设计目标和功能如下：
1、	能添加、删除、修改、查询备忘录内容；
2、	能查看备忘录详细内容；
3、	可以设置闹钟提醒；
具备良好的操作界面，操作简单方便，系统运行稳定。

##架构
采用MVP架构设计
Model层与Presenter层解耦合,Model可以在底层使用sharedPreferences或者SQLite.

###Model层设计

####表
	create table notes(
		id int Primary key,
		title varchar(40) not null,
		content text not null,
		alarm boolean Default false,
		alarm_time datetime
		);


