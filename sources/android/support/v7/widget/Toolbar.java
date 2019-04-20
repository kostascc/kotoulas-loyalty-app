package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.C0291R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuPresenter.Callback;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Toolbar extends ViewGroup {
    private static final String TAG = "Toolbar";
    private Callback mActionMenuPresenterCallback;
    int mButtonGravity;
    ImageButton mCollapseButtonView;
    private CharSequence mCollapseDescription;
    private Drawable mCollapseIcon;
    private boolean mCollapsible;
    private int mContentInsetEndWithActions;
    private int mContentInsetStartWithNavigation;
    private RtlSpacingHelper mContentInsets;
    private boolean mEatingHover;
    private boolean mEatingTouch;
    View mExpandedActionView;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private int mGravity;
    private final ArrayList<View> mHiddenViews;
    private ImageView mLogoView;
    private int mMaxButtonHeight;
    private MenuBuilder.Callback mMenuBuilderCallback;
    private ActionMenuView mMenuView;
    private final android.support.v7.widget.ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
    private ImageButton mNavButtonView;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private ActionMenuPresenter mOuterActionMenuPresenter;
    private Context mPopupContext;
    private int mPopupTheme;
    private final Runnable mShowOverflowMenuRunnable;
    private CharSequence mSubtitleText;
    private int mSubtitleTextAppearance;
    private int mSubtitleTextColor;
    private TextView mSubtitleTextView;
    private final int[] mTempMargins;
    private final ArrayList<View> mTempViews;
    private int mTitleMarginBottom;
    private int mTitleMarginEnd;
    private int mTitleMarginStart;
    private int mTitleMarginTop;
    private CharSequence mTitleText;
    private int mTitleTextAppearance;
    private int mTitleTextColor;
    private TextView mTitleTextView;
    private ToolbarWidgetWrapper mWrapper;

    /* renamed from: android.support.v7.widget.Toolbar$2 */
    class C03652 implements Runnable {
        C03652() {
        }

        public void run() {
            Toolbar.this.showOverflowMenu();
        }
    }

    /* renamed from: android.support.v7.widget.Toolbar$3 */
    class C03663 implements OnClickListener {
        C03663() {
        }

        public void onClick(View view) {
            Toolbar.this.collapseActionView();
        }
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    /* renamed from: android.support.v7.widget.Toolbar$1 */
    class C05491 implements android.support.v7.widget.ActionMenuView.OnMenuItemClickListener {
        C05491() {
        }

        public boolean onMenuItemClick(MenuItem menuItem) {
            return Toolbar.this.mOnMenuItemClickListener != null ? Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(menuItem) : null;
        }
    }

    private class ExpandedActionViewMenuPresenter implements MenuPresenter {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;

        public boolean flagActionItems() {
            return false;
        }

        public int getId() {
            return 0;
        }

        public MenuView getMenuView(ViewGroup viewGroup) {
            return null;
        }

        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        }

        public void onRestoreInstanceState(Parcelable parcelable) {
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
            return false;
        }

        public void setCallback(Callback callback) {
        }

        ExpandedActionViewMenuPresenter() {
        }

        public void initForMenu(Context context, MenuBuilder menuBuilder) {
            context = this.mMenu;
            if (context != null) {
                MenuItemImpl menuItemImpl = this.mCurrentExpandedItem;
                if (menuItemImpl != null) {
                    context.collapseItemActionView(menuItemImpl);
                }
            }
            this.mMenu = menuBuilder;
        }

        public void updateMenuView(boolean z) {
            if (this.mCurrentExpandedItem) {
                z = this.mMenu;
                Object obj = null;
                if (z) {
                    z = z.size();
                    for (boolean z2 = false; z2 < z; z2++) {
                        if (this.mMenu.getItem(z2) == this.mCurrentExpandedItem) {
                            obj = 1;
                            break;
                        }
                    }
                }
                if (obj == null) {
                    collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
                }
            }
        }

        public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            Toolbar.this.ensureCollapseButtonView();
            menuBuilder = Toolbar.this.mCollapseButtonView.getParent();
            MenuBuilder menuBuilder2 = Toolbar.this;
            if (menuBuilder != menuBuilder2) {
                if (menuBuilder instanceof ViewGroup) {
                    ((ViewGroup) menuBuilder).removeView(menuBuilder2.mCollapseButtonView);
                }
                menuBuilder = Toolbar.this;
                menuBuilder.addView(menuBuilder.mCollapseButtonView);
            }
            Toolbar.this.mExpandedActionView = menuItemImpl.getActionView();
            this.mCurrentExpandedItem = menuItemImpl;
            menuBuilder = Toolbar.this.mExpandedActionView.getParent();
            menuBuilder2 = Toolbar.this;
            if (menuBuilder != menuBuilder2) {
                if (menuBuilder instanceof ViewGroup) {
                    ((ViewGroup) menuBuilder).removeView(menuBuilder2.mExpandedActionView);
                }
                menuBuilder = Toolbar.this.generateDefaultLayoutParams();
                menuBuilder.gravity = 8388611 | (Toolbar.this.mButtonGravity & 112);
                menuBuilder.mViewType = 2;
                Toolbar.this.mExpandedActionView.setLayoutParams(menuBuilder);
                menuBuilder = Toolbar.this;
                menuBuilder.addView(menuBuilder.mExpandedActionView);
            }
            Toolbar.this.removeChildrenForExpandedActionView();
            Toolbar.this.requestLayout();
            menuItemImpl.setActionViewExpanded(true);
            if ((Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) != null) {
                ((CollapsibleActionView) Toolbar.this.mExpandedActionView).onActionViewExpanded();
            }
            return true;
        }

        public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            if ((Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) != null) {
                ((CollapsibleActionView) Toolbar.this.mExpandedActionView).onActionViewCollapsed();
            }
            menuBuilder = Toolbar.this;
            menuBuilder.removeView(menuBuilder.mExpandedActionView);
            menuBuilder = Toolbar.this;
            menuBuilder.removeView(menuBuilder.mCollapseButtonView);
            menuBuilder = Toolbar.this;
            menuBuilder.mExpandedActionView = null;
            menuBuilder.addChildrenForExpandedActionView();
            this.mCurrentExpandedItem = null;
            Toolbar.this.requestLayout();
            menuItemImpl.setActionViewExpanded(null);
            return true;
        }
    }

    public static class LayoutParams extends android.support.v7.app.ActionBar.LayoutParams {
        static final int CUSTOM = 0;
        static final int EXPANDED = 2;
        static final int SYSTEM = 1;
        int mViewType;

        public LayoutParams(@NonNull Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.mViewType = null;
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.mViewType = 0;
            this.gravity = 8388627;
        }

        public LayoutParams(int i, int i2, int i3) {
            super(i, i2);
            this.mViewType = 0;
            this.gravity = i3;
        }

        public LayoutParams(int i) {
            this(-2, -1, i);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((android.support.v7.app.ActionBar.LayoutParams) layoutParams);
            this.mViewType = 0;
            this.mViewType = layoutParams.mViewType;
        }

        public LayoutParams(android.support.v7.app.ActionBar.LayoutParams layoutParams) {
            super(layoutParams);
            this.mViewType = null;
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super((android.view.ViewGroup.LayoutParams) marginLayoutParams);
            this.mViewType = 0;
            copyMarginsFromCompat(marginLayoutParams);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.mViewType = null;
        }

        void copyMarginsFromCompat(MarginLayoutParams marginLayoutParams) {
            this.leftMargin = marginLayoutParams.leftMargin;
            this.topMargin = marginLayoutParams.topMargin;
            this.rightMargin = marginLayoutParams.rightMargin;
            this.bottomMargin = marginLayoutParams.bottomMargin;
        }
    }

    public static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = new C03671();
        int expandedMenuItemId;
        boolean isOverflowOpen;

        /* renamed from: android.support.v7.widget.Toolbar$SavedState$1 */
        static class C03671 implements ClassLoaderCreator<SavedState> {
            C03671() {
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        }

        public SavedState(Parcel parcel) {
            this(parcel, null);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.expandedMenuItemId = parcel.readInt();
            this.isOverflowOpen = parcel.readInt() != null ? true : null;
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.expandedMenuItemId);
            parcel.writeInt(this.isOverflowOpen);
        }
    }

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, C0291R.attr.toolbarStyle);
    }

    public Toolbar(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mGravity = 8388627;
        this.mTempViews = new ArrayList();
        this.mHiddenViews = new ArrayList();
        this.mTempMargins = new int[2];
        this.mMenuViewItemClickListener = new C05491();
        this.mShowOverflowMenuRunnable = new C03652();
        context = TintTypedArray.obtainStyledAttributes(getContext(), attributeSet, C0291R.styleable.Toolbar, i, 0);
        this.mTitleTextAppearance = context.getResourceId(C0291R.styleable.Toolbar_titleTextAppearance, 0);
        this.mSubtitleTextAppearance = context.getResourceId(C0291R.styleable.Toolbar_subtitleTextAppearance, 0);
        this.mGravity = context.getInteger(C0291R.styleable.Toolbar_android_gravity, this.mGravity);
        this.mButtonGravity = context.getInteger(C0291R.styleable.Toolbar_buttonGravity, 48);
        attributeSet = context.getDimensionPixelOffset(C0291R.styleable.Toolbar_titleMargin, 0);
        if (context.hasValue(C0291R.styleable.Toolbar_titleMargins) != 0) {
            attributeSet = context.getDimensionPixelOffset(C0291R.styleable.Toolbar_titleMargins, attributeSet);
        }
        this.mTitleMarginBottom = attributeSet;
        this.mTitleMarginTop = attributeSet;
        this.mTitleMarginEnd = attributeSet;
        this.mTitleMarginStart = attributeSet;
        attributeSet = context.getDimensionPixelOffset(C0291R.styleable.Toolbar_titleMarginStart, -1);
        if (attributeSet >= null) {
            this.mTitleMarginStart = attributeSet;
        }
        attributeSet = context.getDimensionPixelOffset(C0291R.styleable.Toolbar_titleMarginEnd, -1);
        if (attributeSet >= null) {
            this.mTitleMarginEnd = attributeSet;
        }
        attributeSet = context.getDimensionPixelOffset(C0291R.styleable.Toolbar_titleMarginTop, -1);
        if (attributeSet >= null) {
            this.mTitleMarginTop = attributeSet;
        }
        attributeSet = context.getDimensionPixelOffset(C0291R.styleable.Toolbar_titleMarginBottom, -1);
        if (attributeSet >= null) {
            this.mTitleMarginBottom = attributeSet;
        }
        this.mMaxButtonHeight = context.getDimensionPixelSize(C0291R.styleable.Toolbar_maxButtonHeight, -1);
        attributeSet = context.getDimensionPixelOffset(C0291R.styleable.Toolbar_contentInsetStart, Integer.MIN_VALUE);
        int dimensionPixelOffset = context.getDimensionPixelOffset(C0291R.styleable.Toolbar_contentInsetEnd, Integer.MIN_VALUE);
        int dimensionPixelSize = context.getDimensionPixelSize(C0291R.styleable.Toolbar_contentInsetLeft, 0);
        int dimensionPixelSize2 = context.getDimensionPixelSize(C0291R.styleable.Toolbar_contentInsetRight, 0);
        ensureContentInsets();
        this.mContentInsets.setAbsolute(dimensionPixelSize, dimensionPixelSize2);
        if (!(attributeSet == -2147483648 && dimensionPixelOffset == Integer.MIN_VALUE)) {
            this.mContentInsets.setRelative(attributeSet, dimensionPixelOffset);
        }
        this.mContentInsetStartWithNavigation = context.getDimensionPixelOffset(C0291R.styleable.Toolbar_contentInsetStartWithNavigation, Integer.MIN_VALUE);
        this.mContentInsetEndWithActions = context.getDimensionPixelOffset(C0291R.styleable.Toolbar_contentInsetEndWithActions, Integer.MIN_VALUE);
        this.mCollapseIcon = context.getDrawable(C0291R.styleable.Toolbar_collapseIcon);
        this.mCollapseDescription = context.getText(C0291R.styleable.Toolbar_collapseContentDescription);
        CharSequence text = context.getText(C0291R.styleable.Toolbar_title);
        if (!TextUtils.isEmpty(text)) {
            setTitle(text);
        }
        text = context.getText(C0291R.styleable.Toolbar_subtitle);
        if (!TextUtils.isEmpty(text)) {
            setSubtitle(text);
        }
        this.mPopupContext = getContext();
        setPopupTheme(context.getResourceId(C0291R.styleable.Toolbar_popupTheme, 0));
        Drawable drawable = context.getDrawable(C0291R.styleable.Toolbar_navigationIcon);
        if (drawable != null) {
            setNavigationIcon(drawable);
        }
        text = context.getText(C0291R.styleable.Toolbar_navigationContentDescription);
        if (!TextUtils.isEmpty(text)) {
            setNavigationContentDescription(text);
        }
        drawable = context.getDrawable(C0291R.styleable.Toolbar_logo);
        if (drawable != null) {
            setLogo(drawable);
        }
        text = context.getText(C0291R.styleable.Toolbar_logoDescription);
        if (!TextUtils.isEmpty(text)) {
            setLogoDescription(text);
        }
        if (context.hasValue(C0291R.styleable.Toolbar_titleTextColor) != null) {
            setTitleTextColor(context.getColor(C0291R.styleable.Toolbar_titleTextColor, -1));
        }
        if (context.hasValue(C0291R.styleable.Toolbar_subtitleTextColor) != null) {
            setSubtitleTextColor(context.getColor(C0291R.styleable.Toolbar_subtitleTextColor, -1));
        }
        context.recycle();
    }

    public void setPopupTheme(@StyleRes int i) {
        if (this.mPopupTheme != i) {
            this.mPopupTheme = i;
            if (i == 0) {
                this.mPopupContext = getContext();
            } else {
                this.mPopupContext = new ContextThemeWrapper(getContext(), i);
            }
        }
    }

    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    public void setTitleMargin(int i, int i2, int i3, int i4) {
        this.mTitleMarginStart = i;
        this.mTitleMarginTop = i2;
        this.mTitleMarginEnd = i3;
        this.mTitleMarginBottom = i4;
        requestLayout();
    }

    public int getTitleMarginStart() {
        return this.mTitleMarginStart;
    }

    public void setTitleMarginStart(int i) {
        this.mTitleMarginStart = i;
        requestLayout();
    }

    public int getTitleMarginTop() {
        return this.mTitleMarginTop;
    }

    public void setTitleMarginTop(int i) {
        this.mTitleMarginTop = i;
        requestLayout();
    }

    public int getTitleMarginEnd() {
        return this.mTitleMarginEnd;
    }

    public void setTitleMarginEnd(int i) {
        this.mTitleMarginEnd = i;
        requestLayout();
    }

    public int getTitleMarginBottom() {
        return this.mTitleMarginBottom;
    }

    public void setTitleMarginBottom(int i) {
        this.mTitleMarginBottom = i;
        requestLayout();
    }

    public void onRtlPropertiesChanged(int i) {
        if (VERSION.SDK_INT >= 17) {
            super.onRtlPropertiesChanged(i);
        }
        ensureContentInsets();
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        boolean z = true;
        if (i != 1) {
            z = false;
        }
        rtlSpacingHelper.setDirection(z);
    }

    public void setLogo(@DrawableRes int i) {
        setLogo(AppCompatResources.getDrawable(getContext(), i));
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean canShowOverflowMenu() {
        if (getVisibility() == 0) {
            ActionMenuView actionMenuView = this.mMenuView;
            if (actionMenuView != null && actionMenuView.isOverflowReserved()) {
                return true;
            }
        }
        return false;
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.isOverflowMenuShowing();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean isOverflowMenuShowPending() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.isOverflowMenuShowPending();
    }

    public boolean showOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.showOverflowMenu();
    }

    public boolean hideOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.hideOverflowMenu();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setMenu(MenuBuilder menuBuilder, ActionMenuPresenter actionMenuPresenter) {
        if (menuBuilder != null || this.mMenuView != null) {
            ensureMenuView();
            MenuBuilder peekMenu = this.mMenuView.peekMenu();
            if (peekMenu != menuBuilder) {
                if (peekMenu != null) {
                    peekMenu.removeMenuPresenter(this.mOuterActionMenuPresenter);
                    peekMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
                }
                if (this.mExpandedMenuPresenter == null) {
                    this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
                }
                actionMenuPresenter.setExpandedActionViewsExclusive(true);
                if (menuBuilder != null) {
                    menuBuilder.addMenuPresenter(actionMenuPresenter, this.mPopupContext);
                    menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
                } else {
                    actionMenuPresenter.initForMenu(this.mPopupContext, null);
                    this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, null);
                    actionMenuPresenter.updateMenuView(true);
                    this.mExpandedMenuPresenter.updateMenuView(true);
                }
                this.mMenuView.setPopupTheme(this.mPopupTheme);
                this.mMenuView.setPresenter(actionMenuPresenter);
                this.mOuterActionMenuPresenter = actionMenuPresenter;
            }
        }
    }

    public void dismissPopupMenus() {
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            actionMenuView.dismissPopupMenus();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean isTitleTruncated() {
        TextView textView = this.mTitleTextView;
        if (textView == null) {
            return false;
        }
        Layout layout = textView.getLayout();
        if (layout == null) {
            return false;
        }
        int lineCount = layout.getLineCount();
        for (int i = 0; i < lineCount; i++) {
            if (layout.getEllipsisCount(i) > 0) {
                return true;
            }
        }
        return false;
    }

    public void setLogo(Drawable drawable) {
        if (drawable != null) {
            ensureLogoView();
            if (!isChildOrHidden(this.mLogoView)) {
                addSystemView(this.mLogoView, true);
            }
        } else {
            View view = this.mLogoView;
            if (view != null && isChildOrHidden(view)) {
                removeView(this.mLogoView);
                this.mHiddenViews.remove(this.mLogoView);
            }
        }
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
        }
    }

    public Drawable getLogo() {
        ImageView imageView = this.mLogoView;
        return imageView != null ? imageView.getDrawable() : null;
    }

    public void setLogoDescription(@StringRes int i) {
        setLogoDescription(getContext().getText(i));
    }

    public void setLogoDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            ensureLogoView();
        }
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            imageView.setContentDescription(charSequence);
        }
    }

    public CharSequence getLogoDescription() {
        ImageView imageView = this.mLogoView;
        return imageView != null ? imageView.getContentDescription() : null;
    }

    private void ensureLogoView() {
        if (this.mLogoView == null) {
            this.mLogoView = new AppCompatImageView(getContext());
        }
    }

    public boolean hasExpandedActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        return (expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null) ? false : true;
    }

    public void collapseActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        MenuItemImpl menuItemImpl = expandedActionViewMenuPresenter == null ? null : expandedActionViewMenuPresenter.mCurrentExpandedItem;
        if (menuItemImpl != null) {
            menuItemImpl.collapseActionView();
        }
    }

    public CharSequence getTitle() {
        return this.mTitleText;
    }

    public void setTitle(@StringRes int i) {
        setTitle(getContext().getText(i));
    }

    public void setTitle(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            View view = this.mTitleTextView;
            if (view != null && isChildOrHidden(view)) {
                removeView(this.mTitleTextView);
                this.mHiddenViews.remove(this.mTitleTextView);
            }
        } else {
            if (this.mTitleTextView == null) {
                Context context = getContext();
                this.mTitleTextView = new AppCompatTextView(context);
                this.mTitleTextView.setSingleLine();
                this.mTitleTextView.setEllipsize(TruncateAt.END);
                int i = this.mTitleTextAppearance;
                if (i != 0) {
                    this.mTitleTextView.setTextAppearance(context, i);
                }
                int i2 = this.mTitleTextColor;
                if (i2 != 0) {
                    this.mTitleTextView.setTextColor(i2);
                }
            }
            if (!isChildOrHidden(this.mTitleTextView)) {
                addSystemView(this.mTitleTextView, true);
            }
        }
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setText(charSequence);
        }
        this.mTitleText = charSequence;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitleText;
    }

    public void setSubtitle(@StringRes int i) {
        setSubtitle(getContext().getText(i));
    }

    public void setSubtitle(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            View view = this.mSubtitleTextView;
            if (view != null && isChildOrHidden(view)) {
                removeView(this.mSubtitleTextView);
                this.mHiddenViews.remove(this.mSubtitleTextView);
            }
        } else {
            if (this.mSubtitleTextView == null) {
                Context context = getContext();
                this.mSubtitleTextView = new AppCompatTextView(context);
                this.mSubtitleTextView.setSingleLine();
                this.mSubtitleTextView.setEllipsize(TruncateAt.END);
                int i = this.mSubtitleTextAppearance;
                if (i != 0) {
                    this.mSubtitleTextView.setTextAppearance(context, i);
                }
                int i2 = this.mSubtitleTextColor;
                if (i2 != 0) {
                    this.mSubtitleTextView.setTextColor(i2);
                }
            }
            if (!isChildOrHidden(this.mSubtitleTextView)) {
                addSystemView(this.mSubtitleTextView, true);
            }
        }
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setText(charSequence);
        }
        this.mSubtitleText = charSequence;
    }

    public void setTitleTextAppearance(Context context, @StyleRes int i) {
        this.mTitleTextAppearance = i;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, i);
        }
    }

    public void setSubtitleTextAppearance(Context context, @StyleRes int i) {
        this.mSubtitleTextAppearance = i;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, i);
        }
    }

    public void setTitleTextColor(@ColorInt int i) {
        this.mTitleTextColor = i;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextColor(i);
        }
    }

    public void setSubtitleTextColor(@ColorInt int i) {
        this.mSubtitleTextColor = i;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextColor(i);
        }
    }

    @Nullable
    public CharSequence getNavigationContentDescription() {
        ImageButton imageButton = this.mNavButtonView;
        return imageButton != null ? imageButton.getContentDescription() : null;
    }

    public void setNavigationContentDescription(@StringRes int i) {
        setNavigationContentDescription(i != 0 ? getContext().getText(i) : 0);
    }

    public void setNavigationContentDescription(@Nullable CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            ensureNavButtonView();
        }
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            imageButton.setContentDescription(charSequence);
        }
    }

    public void setNavigationIcon(@DrawableRes int i) {
        setNavigationIcon(AppCompatResources.getDrawable(getContext(), i));
    }

    public void setNavigationIcon(@Nullable Drawable drawable) {
        if (drawable != null) {
            ensureNavButtonView();
            if (!isChildOrHidden(this.mNavButtonView)) {
                addSystemView(this.mNavButtonView, true);
            }
        } else {
            View view = this.mNavButtonView;
            if (view != null && isChildOrHidden(view)) {
                removeView(this.mNavButtonView);
                this.mHiddenViews.remove(this.mNavButtonView);
            }
        }
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            imageButton.setImageDrawable(drawable);
        }
    }

    @Nullable
    public Drawable getNavigationIcon() {
        ImageButton imageButton = this.mNavButtonView;
        return imageButton != null ? imageButton.getDrawable() : null;
    }

    public void setNavigationOnClickListener(OnClickListener onClickListener) {
        ensureNavButtonView();
        this.mNavButtonView.setOnClickListener(onClickListener);
    }

    public Menu getMenu() {
        ensureMenu();
        return this.mMenuView.getMenu();
    }

    public void setOverflowIcon(@Nullable Drawable drawable) {
        ensureMenu();
        this.mMenuView.setOverflowIcon(drawable);
    }

    @Nullable
    public Drawable getOverflowIcon() {
        ensureMenu();
        return this.mMenuView.getOverflowIcon();
    }

    private void ensureMenu() {
        ensureMenuView();
        if (this.mMenuView.peekMenu() == null) {
            MenuBuilder menuBuilder = (MenuBuilder) this.mMenuView.getMenu();
            if (this.mExpandedMenuPresenter == null) {
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
            }
            this.mMenuView.setExpandedActionViewsExclusive(true);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
    }

    private void ensureMenuView() {
        if (this.mMenuView == null) {
            this.mMenuView = new ActionMenuView(getContext());
            this.mMenuView.setPopupTheme(this.mPopupTheme);
            this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
            this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
            android.view.ViewGroup.LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = GravityCompat.END | (this.mButtonGravity & 112);
            this.mMenuView.setLayoutParams(generateDefaultLayoutParams);
            addSystemView(this.mMenuView, false);
        }
    }

    private MenuInflater getMenuInflater() {
        return new SupportMenuInflater(getContext());
    }

    public void inflateMenu(@MenuRes int i) {
        getMenuInflater().inflate(i, getMenu());
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setContentInsetsRelative(int i, int i2) {
        ensureContentInsets();
        this.mContentInsets.setRelative(i, i2);
    }

    public int getContentInsetStart() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getStart() : 0;
    }

    public int getContentInsetEnd() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getEnd() : 0;
    }

    public void setContentInsetsAbsolute(int i, int i2) {
        ensureContentInsets();
        this.mContentInsets.setAbsolute(i, i2);
    }

    public int getContentInsetLeft() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getLeft() : 0;
    }

    public int getContentInsetRight() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getRight() : 0;
    }

    public int getContentInsetStartWithNavigation() {
        int i = this.mContentInsetStartWithNavigation;
        return i != Integer.MIN_VALUE ? i : getContentInsetStart();
    }

    public void setContentInsetStartWithNavigation(int i) {
        if (i < 0) {
            i = Integer.MIN_VALUE;
        }
        if (i != this.mContentInsetStartWithNavigation) {
            this.mContentInsetStartWithNavigation = i;
            if (getNavigationIcon() != 0) {
                requestLayout();
            }
        }
    }

    public int getContentInsetEndWithActions() {
        int i = this.mContentInsetEndWithActions;
        return i != Integer.MIN_VALUE ? i : getContentInsetEnd();
    }

    public void setContentInsetEndWithActions(int i) {
        if (i < 0) {
            i = Integer.MIN_VALUE;
        }
        if (i != this.mContentInsetEndWithActions) {
            this.mContentInsetEndWithActions = i;
            if (getNavigationIcon() != 0) {
                requestLayout();
            }
        }
    }

    public int getCurrentContentInsetStart() {
        if (getNavigationIcon() != null) {
            return Math.max(getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0));
        }
        return getContentInsetStart();
    }

    public int getCurrentContentInsetEnd() {
        Object obj;
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            MenuBuilder peekMenu = actionMenuView.peekMenu();
            obj = (peekMenu == null || !peekMenu.hasVisibleItems()) ? null : 1;
        } else {
            obj = null;
        }
        if (obj != null) {
            return Math.max(getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0));
        }
        return getContentInsetEnd();
    }

    public int getCurrentContentInsetLeft() {
        if (ViewCompat.getLayoutDirection(this) == 1) {
            return getCurrentContentInsetEnd();
        }
        return getCurrentContentInsetStart();
    }

    public int getCurrentContentInsetRight() {
        if (ViewCompat.getLayoutDirection(this) == 1) {
            return getCurrentContentInsetStart();
        }
        return getCurrentContentInsetEnd();
    }

    private void ensureNavButtonView() {
        if (this.mNavButtonView == null) {
            this.mNavButtonView = new AppCompatImageButton(getContext(), null, C0291R.attr.toolbarNavigationButtonStyle);
            android.view.ViewGroup.LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = 8388611 | (this.mButtonGravity & 112);
            this.mNavButtonView.setLayoutParams(generateDefaultLayoutParams);
        }
    }

    void ensureCollapseButtonView() {
        if (this.mCollapseButtonView == null) {
            this.mCollapseButtonView = new AppCompatImageButton(getContext(), null, C0291R.attr.toolbarNavigationButtonStyle);
            this.mCollapseButtonView.setImageDrawable(this.mCollapseIcon);
            this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
            android.view.ViewGroup.LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = 8388611 | (this.mButtonGravity & 112);
            generateDefaultLayoutParams.mViewType = 2;
            this.mCollapseButtonView.setLayoutParams(generateDefaultLayoutParams);
            this.mCollapseButtonView.setOnClickListener(new C03663());
        }
    }

    private void addSystemView(View view, boolean z) {
        android.view.ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = generateDefaultLayoutParams();
        } else if (checkLayoutParams(layoutParams)) {
            layoutParams = (LayoutParams) layoutParams;
        } else {
            layoutParams = generateLayoutParams(layoutParams);
        }
        layoutParams.mViewType = 1;
        if (z && this.mExpandedActionView) {
            view.setLayoutParams(layoutParams);
            this.mHiddenViews.add(view);
            return;
        }
        addView(view, layoutParams);
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable savedState = new SavedState(super.onSaveInstanceState());
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        if (!(expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null)) {
            savedState.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
        }
        savedState.isOverflowOpen = isOverflowMenuShowing();
        return savedState;
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable;
            super.onRestoreInstanceState(savedState.getSuperState());
            ActionMenuView actionMenuView = this.mMenuView;
            Menu peekMenu = actionMenuView != null ? actionMenuView.peekMenu() : null;
            if (!(savedState.expandedMenuItemId == 0 || this.mExpandedMenuPresenter == null || peekMenu == null)) {
                MenuItem findItem = peekMenu.findItem(savedState.expandedMenuItemId);
                if (findItem != null) {
                    findItem.expandActionView();
                }
            }
            if (savedState.isOverflowOpen != null) {
                postShowOverflowMenu();
            }
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    private void postShowOverflowMenu() {
        removeCallbacks(this.mShowOverflowMenuRunnable);
        post(this.mShowOverflowMenuRunnable);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mShowOverflowMenuRunnable);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            motionEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && motionEvent == null) {
                this.mEatingTouch = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.mEatingTouch = false;
        }
        return true;
    }

    public boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 9) {
            this.mEatingHover = false;
        }
        if (!this.mEatingHover) {
            motionEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && motionEvent == null) {
                this.mEatingHover = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.mEatingHover = false;
        }
        return true;
    }

    private void measureChildConstrained(View view, int i, int i2, int i3, int i4, int i5) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        i = getChildMeasureSpec(i, (((getPaddingLeft() + getPaddingRight()) + marginLayoutParams.leftMargin) + marginLayoutParams.rightMargin) + i2, marginLayoutParams.width);
        i2 = getChildMeasureSpec(i3, (((getPaddingTop() + getPaddingBottom()) + marginLayoutParams.topMargin) + marginLayoutParams.bottomMargin) + i4, marginLayoutParams.height);
        i3 = MeasureSpec.getMode(i2);
        if (i3 != 1073741824 && i5 >= 0) {
            if (i3 != 0) {
                i5 = Math.min(MeasureSpec.getSize(i2), i5);
            }
            i2 = MeasureSpec.makeMeasureSpec(i5, 1073741824);
        }
        view.measure(i, i2);
    }

    private int measureChildCollapseMargins(View view, int i, int i2, int i3, int i4, int[] iArr) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        int i5 = marginLayoutParams.leftMargin - iArr[0];
        int i6 = marginLayoutParams.rightMargin - iArr[1];
        int max = Math.max(0, i5) + Math.max(0, i6);
        iArr[0] = Math.max(0, -i5);
        iArr[1] = Math.max(0, -i6);
        view.measure(getChildMeasureSpec(i, ((getPaddingLeft() + getPaddingRight()) + max) + i2, marginLayoutParams.width), getChildMeasureSpec(i3, (((getPaddingTop() + getPaddingBottom()) + marginLayoutParams.topMargin) + marginLayoutParams.bottomMargin) + i4, marginLayoutParams.height));
        return view.getMeasuredWidth() + max;
    }

    private boolean shouldCollapse() {
        if (!this.mCollapsible) {
            return false;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (shouldLayout(childAt) && childAt.getMeasuredWidth() > 0 && childAt.getMeasuredHeight() > 0) {
                return false;
            }
        }
        return true;
    }

    protected void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int measuredWidth;
        int max;
        int combineMeasuredStates;
        int measuredHeight;
        int combineMeasuredStates2;
        int[] iArr = this.mTempMargins;
        if (ViewUtils.isLayoutRtl(this)) {
            i3 = 1;
            i4 = 0;
        } else {
            i3 = 0;
            i4 = 1;
        }
        if (shouldLayout(r7.mNavButtonView)) {
            measureChildConstrained(r7.mNavButtonView, i, 0, i2, 0, r7.mMaxButtonHeight);
            measuredWidth = r7.mNavButtonView.getMeasuredWidth() + getHorizontalMargins(r7.mNavButtonView);
            max = Math.max(0, r7.mNavButtonView.getMeasuredHeight() + getVerticalMargins(r7.mNavButtonView));
            combineMeasuredStates = View.combineMeasuredStates(0, r7.mNavButtonView.getMeasuredState());
        } else {
            measuredWidth = 0;
            max = 0;
            combineMeasuredStates = 0;
        }
        if (shouldLayout(r7.mCollapseButtonView)) {
            measureChildConstrained(r7.mCollapseButtonView, i, 0, i2, 0, r7.mMaxButtonHeight);
            measuredWidth = r7.mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins(r7.mCollapseButtonView);
            max = Math.max(max, r7.mCollapseButtonView.getMeasuredHeight() + getVerticalMargins(r7.mCollapseButtonView));
            combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, r7.mCollapseButtonView.getMeasuredState());
        }
        int currentContentInsetStart = getCurrentContentInsetStart();
        int max2 = 0 + Math.max(currentContentInsetStart, measuredWidth);
        iArr[i3] = Math.max(0, currentContentInsetStart - measuredWidth);
        if (shouldLayout(r7.mMenuView)) {
            measureChildConstrained(r7.mMenuView, i, max2, i2, 0, r7.mMaxButtonHeight);
            measuredWidth = r7.mMenuView.getMeasuredWidth() + getHorizontalMargins(r7.mMenuView);
            max = Math.max(max, r7.mMenuView.getMeasuredHeight() + getVerticalMargins(r7.mMenuView));
            combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, r7.mMenuView.getMeasuredState());
        } else {
            measuredWidth = 0;
        }
        currentContentInsetStart = getCurrentContentInsetEnd();
        i3 = max2 + Math.max(currentContentInsetStart, measuredWidth);
        iArr[i4] = Math.max(0, currentContentInsetStart - measuredWidth);
        if (shouldLayout(r7.mExpandedActionView)) {
            i3 += measureChildCollapseMargins(r7.mExpandedActionView, i, i3, i2, 0, iArr);
            max = Math.max(max, r7.mExpandedActionView.getMeasuredHeight() + getVerticalMargins(r7.mExpandedActionView));
            combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, r7.mExpandedActionView.getMeasuredState());
        }
        if (shouldLayout(r7.mLogoView)) {
            i3 += measureChildCollapseMargins(r7.mLogoView, i, i3, i2, 0, iArr);
            max = Math.max(max, r7.mLogoView.getMeasuredHeight() + getVerticalMargins(r7.mLogoView));
            combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, r7.mLogoView.getMeasuredState());
        }
        i4 = getChildCount();
        max2 = max;
        max = i3;
        for (i3 = 0; i3 < i4; i3++) {
            View childAt = getChildAt(i3);
            if (((LayoutParams) childAt.getLayoutParams()).mViewType == 0) {
                if (shouldLayout(childAt)) {
                    max += measureChildCollapseMargins(childAt, i, max, i2, 0, iArr);
                    max2 = Math.max(max2, childAt.getMeasuredHeight() + getVerticalMargins(childAt));
                    combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, childAt.getMeasuredState());
                }
            }
        }
        i3 = r7.mTitleMarginTop + r7.mTitleMarginBottom;
        i4 = r7.mTitleMarginStart + r7.mTitleMarginEnd;
        if (shouldLayout(r7.mTitleTextView)) {
            measureChildCollapseMargins(r7.mTitleTextView, i, max + i4, i2, i3, iArr);
            measuredWidth = r7.mTitleTextView.getMeasuredWidth() + getHorizontalMargins(r7.mTitleTextView);
            measuredHeight = r7.mTitleTextView.getMeasuredHeight() + getVerticalMargins(r7.mTitleTextView);
            combineMeasuredStates2 = View.combineMeasuredStates(combineMeasuredStates, r7.mTitleTextView.getMeasuredState());
            combineMeasuredStates = measuredWidth;
        } else {
            combineMeasuredStates2 = combineMeasuredStates;
            combineMeasuredStates = 0;
            measuredHeight = 0;
        }
        if (shouldLayout(r7.mSubtitleTextView)) {
            int i5 = measuredHeight + i3;
            i3 = combineMeasuredStates2;
            combineMeasuredStates = Math.max(combineMeasuredStates, measureChildCollapseMargins(r7.mSubtitleTextView, i, max + i4, i2, i5, iArr));
            measuredHeight += r7.mSubtitleTextView.getMeasuredHeight() + getVerticalMargins(r7.mSubtitleTextView);
            combineMeasuredStates2 = View.combineMeasuredStates(i3, r7.mSubtitleTextView.getMeasuredState());
        } else {
            i3 = combineMeasuredStates2;
        }
        max += combineMeasuredStates;
        measuredWidth = Math.max(max2, measuredHeight) + (getPaddingTop() + getPaddingBottom());
        int i6 = i;
        currentContentInsetStart = View.resolveSizeAndState(Math.max(max + (getPaddingLeft() + getPaddingRight()), getSuggestedMinimumWidth()), i6, ViewCompat.MEASURED_STATE_MASK & combineMeasuredStates2);
        measuredWidth = View.resolveSizeAndState(Math.max(measuredWidth, getSuggestedMinimumHeight()), i2, combineMeasuredStates2 << 16);
        if (shouldCollapse()) {
            measuredWidth = 0;
        }
        setMeasuredDimension(currentContentInsetStart, measuredWidth);
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int measuredHeight;
        LayoutParams layoutParams;
        int i7;
        int size;
        Object obj;
        Toolbar toolbar = this;
        Object obj2 = ViewCompat.getLayoutDirection(this) == 1 ? 1 : null;
        int width = getWidth();
        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int i8 = width - paddingRight;
        int[] iArr = toolbar.mTempMargins;
        iArr[1] = 0;
        iArr[0] = 0;
        int minimumHeight = ViewCompat.getMinimumHeight(this);
        minimumHeight = minimumHeight >= 0 ? Math.min(minimumHeight, i4 - i2) : 0;
        if (!shouldLayout(toolbar.mNavButtonView)) {
            i5 = paddingLeft;
            i6 = i8;
        } else if (obj2 != null) {
            i6 = layoutChildRight(toolbar.mNavButtonView, i8, iArr, minimumHeight);
            i5 = paddingLeft;
        } else {
            i5 = layoutChildLeft(toolbar.mNavButtonView, paddingLeft, iArr, minimumHeight);
            i6 = i8;
        }
        if (shouldLayout(toolbar.mCollapseButtonView)) {
            if (obj2 != null) {
                i6 = layoutChildRight(toolbar.mCollapseButtonView, i6, iArr, minimumHeight);
            } else {
                i5 = layoutChildLeft(toolbar.mCollapseButtonView, i5, iArr, minimumHeight);
            }
        }
        if (shouldLayout(toolbar.mMenuView)) {
            if (obj2 != null) {
                i5 = layoutChildLeft(toolbar.mMenuView, i5, iArr, minimumHeight);
            } else {
                i6 = layoutChildRight(toolbar.mMenuView, i6, iArr, minimumHeight);
            }
        }
        int currentContentInsetLeft = getCurrentContentInsetLeft();
        int currentContentInsetRight = getCurrentContentInsetRight();
        iArr[0] = Math.max(0, currentContentInsetLeft - i5);
        iArr[1] = Math.max(0, currentContentInsetRight - (i8 - i6));
        int max = Math.max(i5, currentContentInsetLeft);
        i8 = Math.min(i6, i8 - currentContentInsetRight);
        if (shouldLayout(toolbar.mExpandedActionView)) {
            if (obj2 != null) {
                i8 = layoutChildRight(toolbar.mExpandedActionView, i8, iArr, minimumHeight);
            } else {
                max = layoutChildLeft(toolbar.mExpandedActionView, max, iArr, minimumHeight);
            }
        }
        if (shouldLayout(toolbar.mLogoView)) {
            if (obj2 != null) {
                i8 = layoutChildRight(toolbar.mLogoView, i8, iArr, minimumHeight);
            } else {
                max = layoutChildLeft(toolbar.mLogoView, max, iArr, minimumHeight);
            }
        }
        boolean shouldLayout = shouldLayout(toolbar.mTitleTextView);
        boolean shouldLayout2 = shouldLayout(toolbar.mSubtitleTextView);
        if (shouldLayout) {
            LayoutParams layoutParams2 = (LayoutParams) toolbar.mTitleTextView.getLayoutParams();
            i3 = paddingRight;
            measuredHeight = ((layoutParams2.topMargin + toolbar.mTitleTextView.getMeasuredHeight()) + layoutParams2.bottomMargin) + 0;
        } else {
            i3 = paddingRight;
            measuredHeight = 0;
        }
        if (shouldLayout2) {
            layoutParams = (LayoutParams) toolbar.mSubtitleTextView.getLayoutParams();
            currentContentInsetRight = width;
            measuredHeight += (layoutParams.topMargin + toolbar.mSubtitleTextView.getMeasuredHeight()) + layoutParams.bottomMargin;
        } else {
            currentContentInsetRight = width;
        }
        if (!shouldLayout) {
            if (!shouldLayout2) {
                i7 = paddingLeft;
                i2 = minimumHeight;
                paddingRight = 0;
                addCustomViewsWithGravity(toolbar.mTempViews, 3);
                size = toolbar.mTempViews.size();
                measuredHeight = max;
                for (max = 0; max < size; max++) {
                    measuredHeight = layoutChildLeft((View) toolbar.mTempViews.get(max), measuredHeight, iArr, i2);
                }
                minimumHeight = i2;
                addCustomViewsWithGravity(toolbar.mTempViews, 5);
                size = toolbar.mTempViews.size();
                for (max = 0; max < size; max++) {
                    i8 = layoutChildRight((View) toolbar.mTempViews.get(max), i8, iArr, minimumHeight);
                }
                addCustomViewsWithGravity(toolbar.mTempViews, 1);
                size = getViewListMeasuredWidth(toolbar.mTempViews, iArr);
                max = (i7 + (((currentContentInsetRight - i7) - i3) / 2)) - (size / 2);
                size += max;
                if (max < measuredHeight) {
                    measuredHeight = size <= i8 ? max - (size - i8) : max;
                }
                size = toolbar.mTempViews.size();
                for (paddingRight = 
/*
Method generation error in method: android.support.v7.widget.Toolbar.onLayout(boolean, int, int, int, int):void, dex: classes.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r7_16 'paddingRight' int) = (r7_8 'paddingRight' int), (r7_14 'paddingRight' int), (r7_15 'paddingRight' int), (r7_15 'paddingRight' int) binds: {(r7_15 'paddingRight' int)=B:97:0x0290, (r7_15 'paddingRight' int)=B:98:0x0292, (r7_8 'paddingRight' int)=B:44:0x0125, (r7_14 'paddingRight' int)=B:86:0x0228} in method: android.support.v7.widget.Toolbar.onLayout(boolean, int, int, int, int):void, dex: classes.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:118)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:57)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:118)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:57)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:187)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:320)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:257)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:220)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:75)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:12)
	at jadx.core.ProcessClass.process(ProcessClass.java:40)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/156856360.run(Unknown Source)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:537)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:509)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 28 more

*/

                private int getViewListMeasuredWidth(List<View> list, int[] iArr) {
                    int i = iArr[0];
                    iArr = iArr[1];
                    int size = list.size();
                    int i2 = iArr;
                    int i3 = i;
                    iArr = null;
                    i = 0;
                    while (iArr < size) {
                        View view = (View) list.get(iArr);
                        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                        int i4 = layoutParams.leftMargin - i3;
                        i3 = layoutParams.rightMargin - i2;
                        i2 = Math.max(0, i4);
                        int max = Math.max(0, i3);
                        i4 = Math.max(0, -i4);
                        i += (i2 + view.getMeasuredWidth()) + max;
                        iArr++;
                        i2 = Math.max(0, -i3);
                        i3 = i4;
                    }
                    return i;
                }

                private int layoutChildLeft(View view, int i, int[] iArr, int i2) {
                    LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                    int i3 = layoutParams.leftMargin - iArr[0];
                    i += Math.max(0, i3);
                    iArr[0] = Math.max(0, -i3);
                    iArr = getChildTop(view, i2);
                    i2 = view.getMeasuredWidth();
                    view.layout(i, iArr, i + i2, view.getMeasuredHeight() + iArr);
                    return i + (i2 + layoutParams.rightMargin);
                }

                private int layoutChildRight(View view, int i, int[] iArr, int i2) {
                    LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                    int i3 = layoutParams.rightMargin - iArr[1];
                    i -= Math.max(0, i3);
                    iArr[1] = Math.max(0, -i3);
                    iArr = getChildTop(view, i2);
                    i2 = view.getMeasuredWidth();
                    view.layout(i - i2, iArr, i, view.getMeasuredHeight() + iArr);
                    return i - (i2 + layoutParams.leftMargin);
                }

                private int getChildTop(View view, int i) {
                    LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                    view = view.getMeasuredHeight();
                    i = i > 0 ? (view - i) / 2 : 0;
                    int childVerticalGravity = getChildVerticalGravity(layoutParams.gravity);
                    if (childVerticalGravity == 48) {
                        return getPaddingTop() - i;
                    }
                    if (childVerticalGravity == 80) {
                        return (((getHeight() - getPaddingBottom()) - view) - layoutParams.bottomMargin) - i;
                    }
                    i = getPaddingTop();
                    childVerticalGravity = getPaddingBottom();
                    int height = getHeight();
                    int i2 = (((height - i) - childVerticalGravity) - view) / 2;
                    if (i2 < layoutParams.topMargin) {
                        i2 = layoutParams.topMargin;
                    } else {
                        height = (((height - childVerticalGravity) - view) - i2) - i;
                        if (height < layoutParams.bottomMargin) {
                            i2 = Math.max(0, i2 - (layoutParams.bottomMargin - height));
                        }
                    }
                    return i + i2;
                }

                private int getChildVerticalGravity(int i) {
                    i &= 112;
                    return (i == 16 || i == 48 || i == 80) ? i : this.mGravity & 112;
                }

                private void addCustomViewsWithGravity(List<View> list, int i) {
                    Object obj = ViewCompat.getLayoutDirection(this) == 1 ? 1 : null;
                    int childCount = getChildCount();
                    i = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection(this));
                    list.clear();
                    View childAt;
                    if (obj != null) {
                        for (childCount--; childCount >= 0; childCount--) {
                            childAt = getChildAt(childCount);
                            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                            if (layoutParams.mViewType == 0 && shouldLayout(childAt) && getChildHorizontalGravity(layoutParams.gravity) == i) {
                                list.add(childAt);
                            }
                        }
                        return;
                    }
                    for (int i2 = 0; i2 < childCount; i2++) {
                        childAt = getChildAt(i2);
                        LayoutParams layoutParams2 = (LayoutParams) childAt.getLayoutParams();
                        if (layoutParams2.mViewType == 0 && shouldLayout(childAt) && getChildHorizontalGravity(layoutParams2.gravity) == i) {
                            list.add(childAt);
                        }
                    }
                }

                private int getChildHorizontalGravity(int i) {
                    int layoutDirection = ViewCompat.getLayoutDirection(this);
                    i = GravityCompat.getAbsoluteGravity(i, layoutDirection) & 7;
                    if (i != 1) {
                        int i2 = 3;
                        if (!(i == 3 || i == 5)) {
                            if (layoutDirection == 1) {
                                i2 = 5;
                            }
                            return i2;
                        }
                    }
                    return i;
                }

                private boolean shouldLayout(View view) {
                    return (view == null || view.getParent() != this || view.getVisibility() == 8) ? null : true;
                }

                private int getHorizontalMargins(View view) {
                    MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
                    return MarginLayoutParamsCompat.getMarginStart(marginLayoutParams) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
                }

                private int getVerticalMargins(View view) {
                    MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
                    return marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
                }

                public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
                    return new LayoutParams(getContext(), attributeSet);
                }

                protected LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
                    if (layoutParams instanceof LayoutParams) {
                        return new LayoutParams((LayoutParams) layoutParams);
                    }
                    if (layoutParams instanceof android.support.v7.app.ActionBar.LayoutParams) {
                        return new LayoutParams((android.support.v7.app.ActionBar.LayoutParams) layoutParams);
                    }
                    if (layoutParams instanceof MarginLayoutParams) {
                        return new LayoutParams((MarginLayoutParams) layoutParams);
                    }
                    return new LayoutParams(layoutParams);
                }

                protected LayoutParams generateDefaultLayoutParams() {
                    return new LayoutParams(-2, -2);
                }

                protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
                    return (!super.checkLayoutParams(layoutParams) || (layoutParams instanceof LayoutParams) == null) ? null : true;
                }

                private static boolean isCustomView(View view) {
                    return ((LayoutParams) view.getLayoutParams()).mViewType == null ? true : null;
                }

                @RestrictTo({Scope.LIBRARY_GROUP})
                public DecorToolbar getWrapper() {
                    if (this.mWrapper == null) {
                        this.mWrapper = new ToolbarWidgetWrapper(this, true);
                    }
                    return this.mWrapper;
                }

                void removeChildrenForExpandedActionView() {
                    for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                        View childAt = getChildAt(childCount);
                        if (!(((LayoutParams) childAt.getLayoutParams()).mViewType == 2 || childAt == this.mMenuView)) {
                            removeViewAt(childCount);
                            this.mHiddenViews.add(childAt);
                        }
                    }
                }

                void addChildrenForExpandedActionView() {
                    for (int size = this.mHiddenViews.size() - 1; size >= 0; size--) {
                        addView((View) this.mHiddenViews.get(size));
                    }
                    this.mHiddenViews.clear();
                }

                private boolean isChildOrHidden(View view) {
                    if (view.getParent() != this) {
                        if (this.mHiddenViews.contains(view) == null) {
                            return null;
                        }
                    }
                    return true;
                }

                @RestrictTo({Scope.LIBRARY_GROUP})
                public void setCollapsible(boolean z) {
                    this.mCollapsible = z;
                    requestLayout();
                }

                @RestrictTo({Scope.LIBRARY_GROUP})
                public void setMenuCallbacks(Callback callback, MenuBuilder.Callback callback2) {
                    this.mActionMenuPresenterCallback = callback;
                    this.mMenuBuilderCallback = callback2;
                    ActionMenuView actionMenuView = this.mMenuView;
                    if (actionMenuView != null) {
                        actionMenuView.setMenuCallbacks(callback, callback2);
                    }
                }

                private void ensureContentInsets() {
                    if (this.mContentInsets == null) {
                        this.mContentInsets = new RtlSpacingHelper();
                    }
                }

                ActionMenuPresenter getOuterActionMenuPresenter() {
                    return this.mOuterActionMenuPresenter;
                }

                Context getPopupContext() {
                    return this.mPopupContext;
                }
            }