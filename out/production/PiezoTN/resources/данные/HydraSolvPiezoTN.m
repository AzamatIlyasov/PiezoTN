%HydraSolvPiezoTN.m
% [ Hydra, Piezo_plot ] = HydraSolvPiezoTN(Hydra, Geodezia, SetevNapor)
%�������� ������ 
% Hydra - ������ �� ������� 
%           (�������� �������, �������, �����, ������)
% Geodezia - ��� ������� ����� ������� ������� 
%           (��������, ��������� ������, ��� � ������ ������)% 
% SetevNapor - ����� �������� ������
%��������� ������
% Hydra - ������� ��������������� �������
% Piezo_Plot - ������ ��� ���������� ����������������� �������
function [ Hydra, Piezo_plot ] = HydraSolvPiezoTN(Hydra, Geodezia, SetevNapor)

%filename = 'Input.xls';
%Gidro = xlsread(filename, 1, 'A:E');

if ~isequal(SetevNapor,0) 
    SetevNapor=58.83; % 130 - ��� ��������, 58.83 - ��� ���1
end;

G_str=max(size(Hydra));

%������ ��������������� �������

Hydra(:,6)=Hydra(:,5)./1.5;

% W, �/�
for i=1:G_str
Hydra(i,7)=sqrt((0.00638*(Hydra(i, 6)^2)*2*9.8)/(((Hydra(i,3)/1000)^4)*958^2));
end;

%R��, ��/� ��� �=0.5
for i=1:G_str
Hydra(i,8)=(0.00638*Hydra(i,6)^2)/((1.14+2*log10(Hydra(i,3)/0.5))^2*(Hydra(i,3)/1000)^5*958);
end;

%����, ��
Hydra(:,9)=4;

%Rp., ��/�
for i=1:G_str
Hydra(i,11)=(0.00638*Hydra(i,6)^2)/((1.14+2*log10(Hydra(i,3)/Hydra(i,9)))^2*(Hydra(i,3)/1000)^5*958);
end;

%betta
Hydra(:,10)=Hydra(:,11)./Hydra(:,8);

%H�, ��
Hydra(:,12)=Hydra(:,4).*Hydra(:,11);

%��, ��
Hydra(:,13)=Hydra(:,12).*0.2;

%������, ��
Hydra(:,14)=Hydra(:,12)+Hydra(:,13);

%�� 2-� �������������
Hydra(:,15)=Hydra(:,14).*(2/1000);

summa=0;
%������ ������ �� ��������� �����, �
for i=1:G_str
 summa=summa+Hydra(i,15);
 Hydra(i,16)=summa;
end;

%������������ ����� � ��������� �����, �
Hydra(:,18)=90;

%������������� ����� � ����� ��-�� ��, �
Hydra(:,17)=Hydra(:,18)-Hydra(:,16);

%  
%�����-������ ��������, ���������� � ���������� �������
%
%������ �� ������ �������
Piezo_plot(:,1)=Hydra(:,15)./2;

summa=0;
%������ ��� �������
for i=1:G_str
 summa=summa+Piezo_plot(i,1);
 Piezo_plot(i,2)=summa;
end;

%����� � ������� ������
Piezo_plot(:,8)=SetevNapor;
%Geodezia= xlsread(filename, 2, 'A:C');
Piezo_plot(:,7)=(Geodezia(:,1));

%�������� �����������
Piezo_plot(:,5)=Piezo_plot(:,8)+Piezo_plot(:,2)+Piezo_plot(:,7);

%������ ���������
Piezo_plot(:,4)=Piezo_plot(:,7)+20+Piezo_plot(:,8)+2*sum(Piezo_plot(:,1))-Piezo_plot(:,2);

%���� �����
%��������� ������
Piezo_plot(:,9)=Geodezia(:,2);
%��� ������� ������
Piezo_plot(:,10)=Geodezia(:,3);
%������� ��������� ����
Piezo_plot(:,11)=Piezo_plot(:,10)+3*Piezo_plot(:,9);
%���� �����
Piezo_plot(:,12)=max(Piezo_plot(:,11))+5;

end

