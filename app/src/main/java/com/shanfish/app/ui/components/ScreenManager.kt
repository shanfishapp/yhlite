import androidx.compose.runtime.Composable

/**
 * 页面导航工具类
 */
object Screen {
    // 存储注册的屏幕
    private val screenMap = mutableMapOf<String, @Composable () -> Unit>()
    
    // 当前显示的屏幕ID
    private var currentScreenId: String? = null
    
    // 屏幕变化监听器
    private var onScreenChanged: ((String) -> Unit)? = null
    
    /**
     * 注册屏幕
     * @param screenId 屏幕唯一标识
     * @param content 屏幕的Compose内容
     */
    fun register(screenId: String, content: @Composable () -> Unit) {
        screenMap[screenId] = content
    }
    
    /**
     * 跳转到指定屏幕
     * @param screenId 要跳转的屏幕ID
     * @throws IllegalArgumentException 如果屏幕未注册
     */
    fun jumpTo(screenId: String) {
        if (!screenMap.containsKey(screenId)) {
            throw IllegalArgumentException("Screen with id $screenId is not registered")
        }
        currentScreenId = screenId
        onScreenChanged?.invoke(screenId)
    }
    
    /**
     * 获取当前屏幕的Compose内容
     * @return 当前屏幕的Composable函数
     * @throws IllegalStateException 如果没有当前屏幕
     */
    @Composable
    fun getCurrentScreen(): @Composable () -> Unit {
        val screenId = currentScreenId ?: throw IllegalStateException("No current screen set")
        return screenMap[screenId] ?: throw IllegalStateException("Screen with id $screenId not found")
    }
    
    /**
     * 设置屏幕变化监听器
     * @param listener 监听器函数，参数是新屏幕的ID
     */
    fun setOnScreenChangedListener(listener: (String) -> Unit) {
        onScreenChanged = listener
    }
    
    /**
     * 获取当前屏幕ID
     */
    fun getCurrentScreenId(): String? = currentScreenId
    
    /**
     * 清除所有注册的屏幕
     */
    fun clear() {
        screenMap.clear()
        currentScreenId = null
        onScreenChanged = null
    }
}