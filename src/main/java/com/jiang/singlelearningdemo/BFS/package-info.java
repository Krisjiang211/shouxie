package com.jiang.singlelearningdemo.BFS;


/**
 * 结论:
 * 广度优先遍历用 Queue(队列)实现
 *    - 队列元素:
 * class Node{
 *     OObject obj;//对象实体
 *     List<String> path;//// 路径（记录属性名层级，比如 ["user", "address", "city"]就是JSON中的"user.address.city"）
 *  }
 *    - while循环从队列中不断取出嵌套对象节点
 *    - 队列中取出的节点用for循环遍历所有节点. 并且判断是否需要将此节点加入队列
 *    - 路径:
 *          - 需要存储在队列的元素节点中, 方便下次取出的时候不丢失路径
 *
 *
 * 深度优先遍历用 Stack(栈)实现(Java中用Deque双端队列实现栈)
 * 栈元素:
 * class Node{
 *     Object obj;//对象实体
 *     String name;//节点名称(比如上一层对象给他的属性名)
 * }
 *    - while循环从栈中不断取出嵌套对象节点
 *    - 栈中取出的节点用for循环遍历所有节点. 并且判断是否需要将此节点加入栈
 *
 *    - 路径:
 *          - 如果寻找到目标, 那么直接遍历栈中的元素的name, 就可以得到路径
 */