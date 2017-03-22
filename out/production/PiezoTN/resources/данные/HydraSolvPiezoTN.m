%HydraSolvPiezoTN.m
% [ Hydra, Piezo_plot ] = HydraSolvPiezoTN(Hydra, Geodezia, SetevNapor)
%Исходные данные 
% Hydra - Данные об участке 
%           (Название участка, Диаметр, Длина, Расход)
% Geodezia - Гео отметки земли данного участка 
%           (Геодезия, Этажность зданий, Гео с учетом зданий)% 
% SetevNapor - Напор сетевого насоса
%Расчетные данные
% Hydra - Таблица гидравлического расчета
% Piezo_Plot - Данные для построения пьезометрического графика
function [ Hydra, Piezo_plot ] = HydraSolvPiezoTN(Hydra, Geodezia, SetevNapor)

%filename = 'Input.xls';
%Gidro = xlsread(filename, 1, 'A:E');

if ~isequal(SetevNapor,0) 
    SetevNapor=58.83; % 130 - для Баскуата, 58.83 - для Кот1
end;

G_str=max(size(Hydra));

%начало гидравлического расчета

Hydra(:,6)=Hydra(:,5)./1.5;

% W, м/с
for i=1:G_str
Hydra(i,7)=sqrt((0.00638*(Hydra(i, 6)^2)*2*9.8)/(((Hydra(i,3)/1000)^4)*958^2));
end;

%Rуд, мм/м при К=0.5
for i=1:G_str
Hydra(i,8)=(0.00638*Hydra(i,6)^2)/((1.14+2*log10(Hydra(i,3)/0.5))^2*(Hydra(i,3)/1000)^5*958);
end;

%Кэкв, мм
Hydra(:,9)=4;

%Rp., мм/м
for i=1:G_str
Hydra(i,11)=(0.00638*Hydra(i,6)^2)/((1.14+2*log10(Hydra(i,3)/Hydra(i,9)))^2*(Hydra(i,3)/1000)^5*958);
end;

%betta
Hydra(:,10)=Hydra(:,11)./Hydra(:,8);

%Hл, мм
Hydra(:,12)=Hydra(:,4).*Hydra(:,11);

%Нм, мм
Hydra(:,13)=Hydra(:,12).*0.2;

%Нвсего, мм
Hydra(:,14)=Hydra(:,12)+Hydra(:,13);

%На 2-х трубапроводах
Hydra(:,15)=Hydra(:,14).*(2/1000);

summa=0;
%Потери напора от источника тепла, м
for i=1:G_str
 summa=summa+Hydra(i,15);
 Hydra(i,16)=summa;
end;

%Распологаемы напор у источника тепла, м
Hydra(:,18)=90;

%Распологаемый напор в конце уч-ка Нр, м
Hydra(:,17)=Hydra(:,18)-Hydra(:,16);

%  
%гидро-расчет выполнен, приступаем к построению графика
%
%Потери на каждом участке
Piezo_plot(:,1)=Hydra(:,15)./2;

summa=0;
%Потери для обратки
for i=1:G_str
 summa=summa+Piezo_plot(i,1);
 Piezo_plot(i,2)=summa;
end;

%Напор в сетевом насосе
Piezo_plot(:,8)=SetevNapor;
%Geodezia= xlsread(filename, 2, 'A:C');
Piezo_plot(:,7)=(Geodezia(:,1));

%Обратный трубопровод
Piezo_plot(:,5)=Piezo_plot(:,8)+Piezo_plot(:,2)+Piezo_plot(:,7);

%Прямой трубопрод
Piezo_plot(:,4)=Piezo_plot(:,7)+20+Piezo_plot(:,8)+2*sum(Piezo_plot(:,1))-Piezo_plot(:,2);

%Стат напор
%Этажность зданий
Piezo_plot(:,9)=Geodezia(:,2);
%Гео отметки зданий
Piezo_plot(:,10)=Geodezia(:,3);
%Верхнее положение воды
Piezo_plot(:,11)=Piezo_plot(:,10)+3*Piezo_plot(:,9);
%Стат напор
Piezo_plot(:,12)=max(Piezo_plot(:,11))+5;

end

