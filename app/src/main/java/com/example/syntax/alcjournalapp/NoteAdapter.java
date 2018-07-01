package com.example.syntax.alcjournalapp;



import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.syntax.alcjournalapp.database.NoteEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * We couldn't come up with a good name for this class. Then, we realized
 * that this lesson is about RecyclerView.
 *
 * RecyclerView... Recycling... Saving the planet? Being green? Anyone?
 * #crickets
 *
 * Avoid unnecessary garbage collection by using RecyclerView and ViewHolders.
 *
 * If you don't like our puns, we named this Adapter GreenAdapter because its
 * contents are green.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private static final String TAG = NoteAdapter.class.getSimpleName();

    private final String DATE_FORMAT = "yyyy/MM/dd";
    private static int viewHolderCount;
    final private ItemClickListener mItemClickListener;
    private List<NoteEntry> mNoteEntries;
    private Context mContext;

    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    /**
     *
     * @param context
     * @param listener
     */
    public NoteAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }


    public interface ItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    /**
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout.
     * @return A new NoteViewHolder that holds the View for each list item
     *
     */

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.note_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NoteViewHolder viewHolder = new NoteViewHolder(view);

        return new NoteViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        // Determine the values of the wanted data
        NoteEntry noteEntry = mNoteEntries.get(position);
        String title = noteEntry.getTitle();
        String description = noteEntry.getDescription();
        String content = noteEntry.getContent();
        String updatedAt = dateFormat.format(noteEntry.getUpdatedAt());
        String createdAt = dateFormat.format(noteEntry.getCreatedAt());
        String tags = noteEntry.getTags();
        String author = noteEntry.getAuthor();

//        Set values
        holder.noteTitle.setText(title);
        holder.noteDescription.setText("Description : " + description);
        holder.noteContent.setText(content);
        holder.noteTags.setText("Tags : " + tags);
        holder.noteAuthor.setText("Author : " + author);
        holder.updatedAt.setText("Last updated on " + updatedAt);

    }

    @Override
    public int getItemCount() {

        if (mNoteEntries == null) {
            return 0;
        }
        return mNoteEntries.size();
    }

    public void setNotes(List <NoteEntry> noteEntries) {
        mNoteEntries = noteEntries;
        notifyDataSetChanged();
    }

    public List<NoteEntry> getNotes() {
        return mNoteEntries;
    }

    /**
     * Cache of the children views for a list item.
     */
    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView noteTitle;
        TextView noteDescription;
        TextView noteContent;
        TextView noteTags;
        TextView noteAuthor;
        TextView createdAt;
        TextView updatedAt;


        public NoteViewHolder(View itemView) {
            super(itemView);

            noteTitle = (TextView) itemView.findViewById(R.id.noteTitle);
            noteDescription = (TextView) itemView.findViewById(R.id.noteDescription);
            noteContent = (TextView) itemView.findViewById(R.id.noteContent);
            noteTags = (TextView) itemView.findViewById(R.id.noteTags);
            noteAuthor = (TextView) itemView.findViewById(R.id.author);
//            createdAt = (TextView) itemView.findViewById(R.id.textViewCreatedAt);
            updatedAt = (TextView) itemView.findViewById(R.id.updatedAt);

            Log.d(TAG, "got here ...");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mNoteEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onListItemClick(elementId);
        }
    }

}
