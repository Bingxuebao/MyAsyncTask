# MyAsyncTask
AsyncTask技术练习
通过练习AsyncTask实现对本环节知识的练习及网络技术、ListView知识的回顾
主要涉及到的知识：AsyncTask异步加载、HTTP协议的使用、JSON数据的解析，
   同时在图片加载时也提供了创建子线程进行异步加载的方法
优化方法：1.创建convertView复用缓存好的布局，提高ListView的运行效率；
		2.创建viewHolder用于对控件的实例进行缓存，不用每次都通过									  	findviewbyID来加载；
		3.使用LruCache来缓存数据，不用每次都通过网络获取数据，为用户节省流量以及提升使用流畅性；
新建的工具类：NewsAdapter、ImageLoader、NewBean
