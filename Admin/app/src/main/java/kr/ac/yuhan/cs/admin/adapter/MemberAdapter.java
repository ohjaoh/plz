package kr.ac.yuhan.cs.admin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import kr.ac.yuhan.cs.admin.data.MemberData;
import kr.ac.yuhan.cs.admin.R;


public class MemberAdapter extends BaseAdapter {
    private ArrayList<MemberData> memberList;
    private LayoutInflater inflater;
    private Context context; // Context 추가

    public MemberAdapter(Context context, ArrayList<MemberData> memberList) {
        this.context = context;
        this.memberList = memberList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public Object getItem(int position) {
        return memberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // 뷰홀더 초기화
            convertView = inflater.inflate(R.layout.member_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.numberTextView = convertView.findViewById(R.id.number);
            viewHolder.userIdTextView = convertView.findViewById(R.id.userId);
            viewHolder.userPointTextView = convertView.findViewById(R.id.userPoint);
            viewHolder.outBtn = convertView.findViewById(R.id.outBtn); // outBtn 초기화
            convertView.setTag(viewHolder);
        } else {
            // 뷰홀더 재사용
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MemberData memberData = memberList.get(position);
        viewHolder.numberTextView.setText(String.valueOf(memberData.getNumber()));
        viewHolder.userIdTextView.setText(memberData.getUserId());
        viewHolder.userPointTextView.setText(String.valueOf(memberData.getPoint()));

        // 삭제 버튼 클릭 이벤트 리스너 설정
        viewHolder.outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AlertDialog 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("해당 데이터를 삭제하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // "예"를 선택했을 때의 동작
                        removeMember(position); // 데이터 삭제
                    }
                });
                builder.setNegativeButton("아니오", null); // "아니오"를 선택했을 때의 동작은 아무것도 하지 않음
                builder.show(); // AlertDialog 표시
            }
        });

        return convertView;
    }

    // 데이터 삭제 메서드
    public void removeMember(int position) {
        memberList.remove(position);
        notifyDataSetChanged(); // 변경된 데이터셋을 알려 ListView를 갱신
    }

    static class ViewHolder {
        TextView numberTextView;
        TextView userIdTextView;
        TextView userPointTextView;
        Button outBtn; // outBtn 추가
    }
}


//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import java.util.List;
//import kr.ac.yuhan.cs.admin.R;
//import kr.ac.yuhan.cs.admin.data.MemberData;
//
//public class MemberAdapter extends ArrayAdapter<MemberData> {
//
//    private Context mContext;
//    private int mResource;
//    private int mBackgroundColor; // 어댑터 항목 뷰의 배경색
//
//    public MemberAdapter(Context context, int resource, List<MemberData> objects, int backgroundColor) {
//        super(context, resource, objects);
//        mContext = context;
//        mResource = resource;
//        mBackgroundColor = backgroundColor; // 배경색 설정
//    }
//
//    @NonNull
//    @Override
//    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        ViewHolder holder;
//
//        if (convertView == null) {
//            LayoutInflater inflater = LayoutInflater.from(mContext);
//            convertView = inflater.inflate(mResource, parent, false);
//            holder = new ViewHolder();
//            holder.numberTextView = convertView.findViewById(R.id.number);
//            holder.userIdTextView = convertView.findViewById(R.id.userId);
//            holder.pointTextView = convertView.findViewById(R.id.userPoint);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        // 배경색 설정
//        convertView.setBackgroundColor(mBackgroundColor);
//
//        // 각 항목의 데이터 가져오기
//        final MemberData member = getItem(position);
//
//        // 뷰에 데이터 설정
//        holder.numberTextView.setText(String.valueOf(member.getNumber()));
//        holder.userIdTextView.setText(member.getUserId());
//        holder.pointTextView.setText(String.valueOf(member.getPoint()));
//
//        // outBtn 클릭 시 삭제 여부를 묻는 모달 창 표시
//        Button outBtn = convertView.findViewById(R.id.outBtn);
//        outBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDeleteConfirmationDialog(position);
//            }
//        });
//
//        return convertView;
//    }
//
//    // 삭제 여부를 묻는 모달 창을 표시하는 메서드
//    private void showDeleteConfirmationDialog(final int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setTitle("삭제 확인");
//        builder.setMessage("정말로 삭제하시겠습니까?");
//        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // 사용자가 '예'를 선택한 경우, 해당 위치의 아이템을 어댑터에서 삭제
//                remove(getItem(position));
//                // 변경된 데이터를 어댑터에 알려줌
//                notifyDataSetChanged();
//            }
//        });
//        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // 사용자가 '아니오'를 선택한 경우, 모달 창을 닫음 (아무 작업도 필요하지 않음)
//            }
//        });
//        builder.show();
//    }
//
//    private static class ViewHolder {
//        TextView numberTextView;
//        TextView userIdTextView;
//        TextView pointTextView;
//    }
//}
