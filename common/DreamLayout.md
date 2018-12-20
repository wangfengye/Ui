## 星空发现图(仿蜗牛梦话圈)
 
 ### 最终效果
 
 ![效果图](./screenshot/woniu.gif)
 
 ### 直接使用

 1. 继承 `BaseAdapter` 实现`onCreateCenter(ViewGroup parent)` `onCreateChild(ViewGroup parent, T t)`方法设置中心View,子View.
 2. DreamLayout实例 调用 `setAdapter(BaseAdapter baseAdapter)` 方法.
 3. 调用`BaseAdapter` 的 ` addChild(T t)`,`removeChild(T t)`增删子View.
 
 ### 需求分解
 
 - 背景类水波纹的扩散效果
	- 初始化
	
	> 初始化没啥好说的,就是一些画笔设置,点位初始化,因为部分初始化与控件大小相关,因此要讲初始化放在尺寸确认后,了解一下View的关键生命周期` 构造函数() --> onFinishInflate() --> onAttachedToWindow() --> onMeasure() --> onSizeChanged() --> onLayout() --> onDraw() --> onDetackedFromWindow()`
	当onSizeChanged之后,布局大小确定下来,因此在此处初始化.
	
	- 环形渐变效果 使用RadialGradient
	
	```
	    //圆心为画布中心,半径为 mRadiusMax,半径80%处向外渐变为白色
        RadialGradient gradient =
                new RadialGradient(width / 2, height / 2, mRadiusMax,
                        new int[]{centerColor, centerColor, edgeColor}, new float[]{0f, .8f, 1.0f}, Shader.TileMode.MIRROR);
        mLayoutPaint.setShader(gradient);
	```
	
	- 使用handler 定时,更新圆直径,模拟水波纹效果
	
	> 初始化三个大小不等的圆,每次放大2%,超出最大值,重新开始,模拟水波纹效果
	
	- 绘制图形,使用画布缩放绘制不同大小的圆环;
	
	>原本直接想用`drawCircle()`绘制不同大小的圆,出现一个问题,需要为不同大小的圆重新 设置环形渐变,而`onDraw()`是一个频繁操作,不建议创建对象,因此想到通过缩放画布的方式来实现绘制不同大小的渐变圆环.
	
	```
		// scale 描述图片放大比例;
        // 将画布放大scale 比例后,绘制同样大小的圆,还原画布,即可得放大scale倍的圆
        canvas.save();
        canvas.scale(scale, scale, width / 2, height / 2);
        canvas.drawCircle(width / 2, height / 2, mRadiusMax, mLayoutPaint);
        canvas.restore();
	```
	
 
 - 随机显示位置的子控件
	- 随机点位:保证不重叠;
	
	> 对于圆形图,不重叠即保证生成的新点与已存在的点距离大于两者半径和
	随机生成点,计算其与已存在在的控件比较,不符合条件,重新生成,超出重试次数即认为无足够空间.
	
	```
	    /**
     * 获取随机点,根据父子空间半径判断随机点是否有足够空间可用
     * @param rad 子控件半径
     * @param radMax 父布局半径
     * @param centerX 父布局中点 x
     * @param centerY 父布局中点 y
     * @return 一个可用的随机点位
     */
    private Point getRandomPoint(int rad, int radMax, int centerX, int centerY) {
        int x = 0;
        int y = 0;
        int counter = 0;
        boolean conflict;
        Random random = new Random();
        while (counter < 100000) {
            counter++;
            x = (int) (getMeasuredWidth() / 2 - radMax + rad + random.nextInt( 2 * radMax - 2 * rad));
            y = (int) (getMeasuredHeight() / 2 - radMax + rad + random.nextInt( 2 * radMax - 2 * rad));
            conflict = false;
            // 判断与其他子控件是否冲突
            for (Point p : mChildrenPoints) {
                int dis = (int) Math.sqrt(Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2));
                if (dis < 2 * rad * (1 + mBlankPer)) {//子控件大小一致,设置两个子控件中点距离最小为 半径和的(1+mBlankPer)倍
                    conflict = true;
                    break;
                }
            }
            if (!conflict) {// 判断与中心点冲突
                int dis = (int) Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2));
                if (dis < (rad + mCenterRadius) * ((1 + mBlankPer))) conflict = true;
            }
            if (!conflict) return new Point(x, y);
        }
        Log.e(TAG, "getRandomPoint: 无剩余空间");
        return new Point(0, 0);
    }
	```
	
	- 测量大小:交给子控件自己测量,保证最大值不超过布局的宽高较小值即可
	
	```
	 @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int contentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int contentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int len = Math.min(contentWidth, contentHeight);
        measureChildren(MeasureSpec.makeMeasureSpec(len, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(len, MeasureSpec.AT_MOST));
    }
	```
	
	- 布局 onLayout()
	
	```
	        for (int i = 1; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int rad = Math.max(view.getMeasuredWidth(), view.getMeasuredHeight()) / 2;
            
            Point position;
			// 数据只支持末尾插入,可直接通过列表长度判断是否是新增点
            if (i > mChildrenPoints.size()) {// 新增的控件,设置随机点
                position = getRandomPoint(rad, mRadiusMax, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                mChildrenPoints.add(position);
            } else {//已存在控件,直接从列表取
                position = mChildrenPoints.get(i - 1);
            }
            int x = position.x;
            int y = position.y;
            view.layout(x - rad, y - rad, x + rad, y + rad);
        }
	```
	
### 大功告成

至此为止,这个小布局的核心功能就完成了. [源码导航](https://github.com/wangfengye/Ui/blob/master/common/Readme.md)
	
	
