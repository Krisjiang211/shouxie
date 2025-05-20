/**
 * 这个主要是装饰器模式
 * 使用装饰器模式, 为@RequestBody注解添加时间戳------>@ReqBodyTime
 * - 需要配备相应的处理器, 即ReqBodyTimeResolver
 * 由于是建立在@RequestBody注解之上的, 所以需要在RequestMappingHandlerAdapter中拿到和处理@RequestBody注解一样的处理器
 * 然后在其返回结果中, 如果是Map类型, 则添加时间戳
 */

package com.jiang.singlelearningdemo.enhance;